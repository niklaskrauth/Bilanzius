private String convertToBitcoin()
{ 
 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, "btc")); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    } 
 
    return localization.getMessage("convert_balance", "Bitcoin", (balance.multiply(exchangeRate))); 
} 
 
private String convertToGermanDeutscheMark() 
{ 
 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, "dem")); 
    BigDecimal balance;
       try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    } 
 
    return localization.getMessage("convert_balance", "Deutsche Mark", (balance.multiply(exchangeRate))); 
} 
 
private String convertToSwissFranc() 
{ 
 
    String currency = "Swiss Franc"; 
    if (localization.getCurrentLanguageCode().equals("de")) { 
        currency = "Schweizer Franken"; 
    } 
 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, "chf")); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    } 
 
    return localization.getMessage("convert_balance", currency, (balance.multiply(exchangeRate))); 
} 
 
private String convertToDogecoin() 
{ 
 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, "doge")); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    }
 
    return localization.getMessage("convert_balance", "Deutsche Mark", (balance.multiply(exchangeRate))); 
} 
 
private String convertToSwissFranc() 
{ 
 
    String currency = "Swiss Franc"; 
    if (localization.getCurrentLanguageCode().equals("de")) { 
        currency = "Schweizer Franken"; 
    } 
 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, "chf")); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    } 
 
    return localization.getMessage("convert_balance", currency, (balance.multiply(exchangeRate))); 
} 
 
private String convertToDogecoin() 
{ 
 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    assert jsonObject != null; 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, "doge")); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    }
}