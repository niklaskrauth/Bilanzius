private String convertCurrency(String currencyCode, String currencyName) 
{ 
    JsonObject jsonObject = Requests.getRequest(currencyUrl); 
    BigDecimal exchangeRate = BigDecimal.valueOf(getCurrencyFromJson(jsonObject, currencyCode)); 
    BigDecimal balance; 
 
    try { 
        balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance(); 
    } catch (DatabaseException e) { 
        return localization.getMessage("database_error", e.toString()); 
    } 
 
    return localization.getMessage("convert_balance", currencyName, (balance.multiply(exchangeRate))); 
} 