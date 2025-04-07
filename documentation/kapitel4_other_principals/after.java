private String convertCurrency(String currencyCode, String currencyName) { 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, currencyCode)); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    } 
 
    return localization.getMessage("convert_balance", currencyName, (balance.multiply(exchangeRate))); 
} 
 
private String convertToBitcoin() 
{ 
    return convertCurrency("btc", "Bitcoin"); 
} 
 
private String convertToGermanDeutscheMark() 
{ 
    return convertCurrency("dem", "Deutsche Mark"); 
} 
 
private String convertToSwissFranc() 
{ 
    String currency = localization.getCurrentLanguageCode().equals("de") ? "Schweizer Franken" : "Swiss Franc"; 
    return convertCurrency("chf", currency); 
} 
 
private String convertToDogecoin() 
{ 
    return convertCurrency("doge", "Dogecoin"); 
} 
 
private String convertToEthereum() 
{ 
    return convertCurrency("eth", "Ethereum"); 
} 
 
private String convertToHongKongDollar() 
{ 
    return convertCurrency("hkd", "Hong Kong Dollar"); 
} 
 
private String convertToJamaicanDollar() 
{ 
    String currency = localization.getCurrentLanguageCode().equals("de") ? "Jamaikanische Dollar" : "Jamaican Dollar"; 
    return convertCurrency("jmd", currency); 
} 
 
private String convertToNorthKoreanWon()
{ 
    String currency = localization.getCurrentLanguageCode().equals("de") ? "Nordkoreanische Won" : "North Korean Won"; 
    return convertCurrency("kpw", currency); 
} 
 
private String convertToRussianRuble()
{ 
    String currency = localization.getCurrentLanguageCode().equals("de") ? "Russischer Rubel" : "Russian Ruble"; 
    return convertCurrency("rub", currency); 
} 
 
private String convertToUsDollar()
{ 
    return convertCurrency("usd", "US Dollar"); 
} 