package fm.doe.national.remote_storage.ui.remote_storage;

import android.content.Context;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.remote_storage.utils.RemoteStorageUtils;
import fm.doe.national.remote_storage.utils.SurveyTextUtil;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DriveStoragePresenter extends BasePresenter<DriveStorageView> {

    private final static Transformer sXmlTransformer;

    static {
        try {
            sXmlTransformer = TransformerFactory.newInstance().newTransformer();
            sXmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            sXmlTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            sXmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            sXmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError();
        }
    }

    private final RemoteStorage storage;
    private final RemoteStorageAccessor accessor;
    private final LocalSettings localSettings;
    private final FilesRepository filesRepository;
    private final Stack<GoogleDriveFileHolder> parentsStack = new Stack<>();
    private final boolean isDebugViewer;
    private final Context appContext;

    public DriveStoragePresenter(RemoteStorageComponent component, CoreComponent coreComponent, boolean isDebugViewer) {
        this.isDebugViewer = isDebugViewer;
        this.storage = component.getRemoteStorage();
        this.accessor = component.getRemoteStorageAccessor();
        this.localSettings = coreComponent.getLocalSettings();
        this.filesRepository = coreComponent.getFilesRepository();
        this.appContext = coreComponent.getContext();
        updateFileHolders();
    }

    public void onItemPressed(GoogleDriveFileHolder item) {
        switch (item.getMimeType()) {
            case FOLDER:
                parentsStack.push(item);
                updateFileHolders();
                break;
            case FILE:
            case XML:
                requestContent(item);
                break;
            case EXCEL:
            case GOOGLE_SHEETS:
                downloadContent(item);
                break;
        }
    }

    private void downloadContent(GoogleDriveFileHolder item) {
        if (!isDebugViewer) {
            return;
        }

        addDisposable(
                RemoteStorageUtils.downloadReport(appContext, storage, filesRepository, item.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(path -> getViewState().showToast(Text.from(R.string.format_file_downloaded, path)), this::handleError)
        );
    }

    private void updateFileHolders() {
        getViewState().setParentName(getCurrentParentName());
        addDisposable(
                storage.requestStorageFiles(getCurrentParentId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(items -> {
                            String currentSurveyPrefix = SurveyTextUtil.convertSurveyTypeToExportPrefix(
                                    localSettings.getSurveyTypeOrDefault()
                            );
                            List<GoogleDriveFileHolder> itemsToShow = items.stream()
                                    .filter(f -> isDebugViewer || f.getMimeType() == DriveType.FOLDER ||
                                            (f.getMimeType() == DriveType.XML && f.getName().startsWith(currentSurveyPrefix)))
                                    .sorted((lv, rv) -> lv.getMimeType().compareTo(rv.getMimeType()))
                                    .collect(Collectors.toList());

                            getViewState().setItems(itemsToShow);
                        }, this::handleError)
        );
    }

    @Nullable
    private String getCurrentParentId() {
        return parentsStack.isEmpty() ? null : parentsStack.peek().getId();
    }

    @Nullable
    private String getCurrentParentName() {
        return parentsStack.isEmpty() ? null : parentsStack.peek().getName();
    }

    private void requestContent(GoogleDriveFileHolder file) {
        addDisposable(
                storage.loadContent(file.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(content -> {
                            if (isDebugViewer) {
                                showDocumentContent(file.getSurveyMetadata().toString(), content);
                            } else {
                                accessor.onContentReceived(content);
                                getViewState().close();
                            }
                        }, this::handleError)
        );
    }

    private void showDocumentContent(String metadata, String content) {
        addDisposable(
                prettyfyXml(content)
                        .flatMap((prettyXml) -> Single.fromCallable(() -> "METADATA:\n\n" +
                                metadata +
                                "\n\n\nCONTENT\n\n" +
                                prettyXml))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(prettyContent -> getViewState().setContent(prettyContent), this::handleError)
        );
    }

    private Single<String> prettyfyXml(String xmlStringToBeFormatted) {
        return Single.fromCallable(() -> {
            try {
                Document document = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(new InputSource(new ByteArrayInputStream(xmlStringToBeFormatted.getBytes(StandardCharsets.UTF_8))));

                // Remove whitespaces outside tags
                document.normalize();
                XPath xPath = XPathFactory.newInstance().newXPath();
                NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
                        document,
                        XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                }

                StringWriter stringWriter = new StringWriter();
                sXmlTransformer.transform(new DOMSource(document), new StreamResult(stringWriter));
                return stringWriter.toString();
            } catch (Exception ex) {
                return xmlStringToBeFormatted;
            }
        });
    }

    public void onBackPressed() {
        if (parentsStack.isEmpty()) {
            accessor.onContentNotReceived();
            getViewState().close();
        } else {
            parentsStack.pop();
            updateFileHolders();
        }
    }

    public void onItemLongPressed(GoogleDriveFileHolder item) {
        addDisposable(
                storage.delete(item.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(this::updateFileHolders, this::handleError)
        );
    }
}
