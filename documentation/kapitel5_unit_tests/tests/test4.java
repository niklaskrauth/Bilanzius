@Test 
void testGetMessageWithParams() 
{ 
    var language = Localization.getInstance(); 
    language.setLocale("en"); 
 
    Assertions.assertEquals("en", language.getCurrentLanguageCode()); 
    Assertions.assertEquals(EXISTING_KEY_WITH_PARAMS_VALUE, language.getMessage(EXISTING_KEY_WITH_PARAMS, "1")); 
} 