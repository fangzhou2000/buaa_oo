
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* MyPerson Tester. 
* 
* @author <Authors name> 
* @since <pre>5ÔÂ 1, 2021</pre> 
* @version 1.0 
*/ 
public class MyPersonTest {
    private static MyPerson myPerson = new MyPerson(1, "test", 100);

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getAcquaintance() 
* 
*/ 
@Test
public void testGetAcquaintance() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getValue() 
* 
*/ 
@Test
public void testGetValue() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getId() 
* 
*/ 
@Test
public void testGetId() throws Exception { 
//TODO: Test goes here...
    Assert.assertEquals(myPerson.getId(), 1);
} 

/** 
* 
* Method: getName() 
* 
*/ 
@Test
public void testGetName() throws Exception { 
//TODO: Test goes here...
    Assert.assertEquals(myPerson.getName(), "test");
} 

/** 
* 
* Method: getAge() 
* 
*/ 
@Test
public void testGetAge() throws Exception { 
//TODO: Test goes here...
    Assert.assertEquals(myPerson.getAge(), 100);
} 

/** 
* 
* Method: equals(Object obj) 
* 
*/ 
@Test
public void testEquals() throws Exception { 
//TODO: Test goes here...
    Assert.assertEquals(myPerson, myPerson);
} 

/** 
* 
* Method: isLinked(Person person) 
* 
*/ 
@Test
public void testIsLinked() throws Exception { 
//TODO: Test goes here...
    Assert.assertTrue(myPerson.isLinked(myPerson));
} 

/** 
* 
* Method: queryValue(Person person) 
* 
*/ 
@Test
public void testQueryValue() throws Exception { 
//TODO: Test goes here...
    Assert.assertEquals(myPerson.queryValue(myPerson), 0);
} 

/** 
* 
* Method: compareTo(Person p2) 
* 
*/ 
@Test
public void testCompareTo() throws Exception { 
//TODO: Test goes here...
    Assert.assertEquals(myPerson.compareTo(myPerson), 0);
} 


} 
