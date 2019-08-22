package fm.doe.national;

import org.junit.Test;

import fm.doe.national.core.utils.TextUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TextUtilUnitTest {
    @Test
    public void test_convertIntToCharsIcons() {
        try {
            TextUtil.convertIntToCharsIcons(-1);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
        assertEquals("a", TextUtil.convertIntToCharsIcons(0));
        assertEquals("aa", TextUtil.convertIntToCharsIcons(24));
        assertEquals("ba", TextUtil.convertIntToCharsIcons(48));
    }
}
