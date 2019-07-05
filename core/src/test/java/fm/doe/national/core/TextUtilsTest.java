package fm.doe.national.core;

import org.junit.Test;

import fm.doe.national.core.utils.TextUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextUtilsTest {

    @Test
    public void testEndsWith() {
        assertTrue(TextUtil.endsWith("usdabgjkbnvlahjglaeuhgiuhgMSG_END", "MSG_END"));
        assertFalse(TextUtil.endsWith("usdabgjkbnvlahjglaeuhgiuhg", "MSG_END"));
        assertFalse(TextUtil.endsWith("usdabgjkbnvlahMSG_ENDjglaeuhgiuhg", "MSG_END"));
        assertFalse(TextUtil.endsWith("MSG_ENDusdabgjkbnvlahjglaeuhgiuhg", "MSG_END"));
        assertTrue(TextUtil.endsWith("MSG_ENDusdabgjkbnvlahjglaeuhgiuhgMSG_END", "MSG_END"));
        assertFalse(TextUtil.endsWith("MSG_ENDusdabgjkbnvlahjglaeuhgiuhgMSGEND", "MSG_END"));
        assertFalse(TextUtil.endsWith("", "MSG_END"));
        assertFalse(TextUtil.endsWith("MSG_EN", "MSG_END"));
        assertTrue(TextUtil.endsWith("MSG_END", "MSG_END"));
    }
}
