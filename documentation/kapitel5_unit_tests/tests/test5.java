@Test 
void testTableElements() 
{ 
    var headComponent = HtmlDataCell.head("head"); 
    var dataComponent = HtmlDataCell.cell("data"); 
 
    Assertions.assertEquals("<th>head</th>", headComponent.build()); 
    Assertions.assertEquals("<td>data</td>", dataComponent.build()); 
}