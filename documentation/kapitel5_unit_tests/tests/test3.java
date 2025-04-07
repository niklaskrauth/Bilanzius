@Test 
void testGetMessage()  
{ 
    var language = Localization.getInstance(); 
    language.setLocale("en"); 
 
    Assertions.assertEquals("en", language.getCurrentLanguageCode()); 
    Assertions.assertEquals(EXISTING_KEY_VALUE, language.getMessage(EXISTING_KEY)); 
    Assertions.assertEquals("MISSING KEY: " + NON_EXISTING_KEY, language.getMessage(NON_EXISTING_KEY)); 
} 