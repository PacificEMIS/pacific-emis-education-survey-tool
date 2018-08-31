package fm.doe.national.data.data_source.models.serializable;

import java.util.ArrayList;
import java.util.List;

public class ListConverter {

    public static <T, R> List<R> createList(List<? extends T> inputList, Converter<T,R> converter) {
        List<R> list = new ArrayList<>(inputList.size());
        for(T input : inputList) {
            list.add(converter.convert(input));
        }
        return list;
    }

   public interface Converter<T, R> {
        R convert(T input);
   }

}
