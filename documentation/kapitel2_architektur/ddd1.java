public static Transaction create(User user, BankAccount account, BigDecimal money, String description)
{
    return new Transaction(0,
            user.getId(), account.getAccountId(), -1, money, Instant.now(), description
    );  // -1 as default categoryId
}
