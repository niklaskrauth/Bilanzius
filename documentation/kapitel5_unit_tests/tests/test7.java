@Test 
void testCreateTransactionInvalidUser() 
{ 
    var service = transactionService(); 
 
    Assertions.assertThrows(DatabaseException.class, () -> service.saveTransaction(new Transaction(1, 5, 1, 1, BigDecimal.ZERO, Instant.now(), "1234"))); 
} 