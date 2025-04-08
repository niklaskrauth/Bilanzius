@Test 
void testDatabaseProvider() throws SQLException 
{ 
    DatabaseProvider.init(new SqlDatabaseServiceRepository(requestBackend())); 
    Assertions.assertNotNull(DatabaseProvider.getBankAccountService()); 
    Assertions.assertNotNull(DatabaseProvider.getUserService()); 
    Assertions.assertNotNull(DatabaseProvider.getTransactionService()); 
    Assertions.assertNotNull(DatabaseProvider.getCategoryService()); 
 
    Assertions.assertThrows(IllegalStateException.class, () -> DatabaseProvider.init(new SqlDatabaseServiceRepository(requestBackend()))); 
} 