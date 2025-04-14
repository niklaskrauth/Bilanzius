@Test
void testCreateTransaction()
{
  var service = transactionService();
  service.saveTransaction(new Transaction(1, 1, 1, 1, BigDecimal.ZERO, Instant.now(), "1234"));
  service.saveTransaction(new Transaction(1, 1, 1, -1, BigDecimal.ZERO, Instant.now(), "1234"));

  var transactions = service.getTransactions(ModelUtils.existingUser(), ModelUtils.existingBankAccount(), 10, 0);
  Assertions.assertEquals(2, transactions.size());
}

private SqliteTransactionService transactionService()
{
  try {
    var backend = requestBackend();
    return new SqliteTransactionService(backend, new MockedBankAccountService());
  } catch (SQLException ex) {
    throw new RuntimeException(ex);
  }
}