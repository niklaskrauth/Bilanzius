@Test
void testCreateCategory()
{
  var mockedService = new MockedCategoryService();
  var command = new CreateCategoryCommand(mockedService, ModelUtils.existingUser());
  command.execute(new String[]{"-n", "test", "-b", "10"});
  Assertions.assertEquals(1, mockedService.getCreatedCategories());
}