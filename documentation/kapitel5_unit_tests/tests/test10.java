@Test 
void testDelete() 
{ 
    var service = bankAccountService(); 
 
    service.createBankAccount(BankAccount.create(ModelUtils.existingUser(), NAME)); 
    service.deleteBankAccount(service.getBankAccount(1).orElseThrow()); 
 
    Assertions.assertTrue(service.getBankAccount(1).isEmpty()); 
} 