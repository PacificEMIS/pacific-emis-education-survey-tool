package fm.doe.national;

import org.junit.Test;

import fm.doe.national.utils.TextUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextUtilUnitTest {
    @Test
    public void test_convertIntToCharsIcons() {
        try {
            TextUtil.convertIntToCharsIcons(-1);
            assertTrue(false);
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
        assertEquals("a", TextUtil.convertIntToCharsIcons(0));
        assertEquals("aa", TextUtil.convertIntToCharsIcons(24));
        assertEquals("ba", TextUtil.convertIntToCharsIcons(48));
    }
}
