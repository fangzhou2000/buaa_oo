
import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

/** 
* MyNetwork Tester. 
* 
* @author <Authors name> 
* @since <pre>5ÔÂ 14, 2021</pre> 
* @version 1.0 
*/ 
public class MyNetworkTest {
    MyPerson person1 = new MyPerson(1, "a", 1);
    MyPerson person2 = new MyPerson(2, "b", 2);
    MyPerson person3 = new MyPerson(3, "c", 10);
    MyPerson person4 = new MyPerson(100, "ab", 100);
    MyPerson person5 = new MyPerson(5, "c", 10);
    MyNetwork network = new MyNetwork();
    MyGroup group1 = new MyGroup(1);
    MyGroup group2 = new MyGroup(2);

@Before
public void before() throws Exception {
    network.addPerson(person1);
    network.addPerson(person2);
    network.addPerson(person3);
    network.addPerson(person4);
    network.addPerson(person5);
    network.addGroup(group1);
    network.addGroup(group2);
    network.addToGroup(1, 1);
    network.addToGroup(2, 1);
    network.addToGroup(3, 2);
    network.addToGroup(5, 2);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: contains(int id) 
* 
*/ 
@Test
public void testContains() throws Exception { 
//TODO: Test goes here...
    assertTrue(network.contains(1));
    assertTrue(network.contains(2));
    assertTrue(network.contains(3));
    assertTrue(network.contains(100));
    assertTrue(network.contains(5));

    assertFalse(network.contains(4));
    assertFalse(network.contains(-1));
} 

/** 
* 
* Method: getPerson(int id) 
* 
*/ 
@Test
public void testGetPerson() throws Exception { 
//TODO: Test goes here...
    assertEquals(person1, network.getPerson(1));
    assertEquals(person2, network.getPerson(2));
    assertEquals(person3, network.getPerson(3));
    assertEquals(person4, network.getPerson(100));
    assertEquals(person5, network.getPerson(5));

    assertNotEquals(person1, network.getPerson(4));
    assertNotEquals(person1, network.getPerson(-1));
} 

/** 
* 
* Method: addPerson(Person person) 
* 
*/ 
@Test
public void testAddPerson() throws Exception { 
//TODO: Test goes here...
    try {
        network.addPerson(person1);
        network.addPerson(new MyPerson(6, "ls", 20));

        assertTrue(network.contains(6));
    } catch (MyEqualPersonIdException e) {
        e.print();
        System.out.println("----1");
    }
} 

/** 
* 
* Method: addRelation(int id1, int id2, int value) 
* 
*/ 
@Test
public void testAddRelation() throws Exception { 
//TODO: Test goes here...
    try {
        network.addRelation(1, 1, 100);
    } catch (MyEqualRelationException e) {
        e.print();
        System.out.println("----2");
    }

    try {
        network.addRelation(1, 2, 100);
    } catch (MyEqualRelationException e) {
        e.print();
        System.out.println("----3");
    }
    assertTrue(person1.isLinked(person2));
    assertTrue(person2.isLinked(person1));
    assertTrue(person1.isLinked(person1));
    assertTrue(person2.isLinked(person2));
    try {
        network.addRelation(-1, -1, 100);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----4");
    }
    try {
        network.addRelation(1, -1, 100);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----5");
    }
    try {
        network.addRelation(-1, 1, 100);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----6");
    }
    try {
        network.addRelation(1, 2, 100);
    } catch (MyEqualRelationException e) {
        e.print();
        System.out.println("----7");
    }
} 

/** 
* 
* Method: queryValue(int id1, int id2) 
* 
*/ 
@Test
public void testQueryValue() throws Exception { 
//TODO: Test goes here...
    try {
        network.queryValue(-1, 1);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----8");
    }
    try {
        network.queryValue(2, 1);
    } catch (MyRelationNotFoundException e) {
        e.print();
        System.out.println("----9");
    }
} 

/** 
* 
* Method: compareName(int id1, int id2) 
* 
*/ 
@Test
public void testCompareName() throws Exception { 
//TODO: Test goes here...
    assertEquals(person1.getName().compareTo(person2.getName()), network.compareName(1, 2));
    try {
        network.compareName(1, -1);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----10");
    }
} 

/** 
* 
* Method: queryPeopleSum() 
* 
*/ 
@Test
public void testQueryPeopleSum() throws Exception { 
//TODO: Test goes here...
    assertEquals(5, network.queryPeopleSum());
} 

/** 
* 
* Method: queryNameRank(int id) 
* 
*/ 
@Test
public void testQueryNameRank() throws Exception { 
//TODO: Test goes here...
    assertEquals(1, network.queryNameRank(1));
    assertEquals(2, network.queryNameRank(100));
    assertEquals(3, network.queryNameRank(2));
    assertEquals(4, network.queryNameRank(3));
    assertEquals(4, network.queryNameRank(5));
    try {
        network.addPerson(new MyPerson(6, "ls", 20));
    } catch (MyEqualPersonIdException e) {
        e.print();
        System.out.println("----11");
    }

    assertTrue(network.contains(6));
    assertEquals(6, network.queryNameRank(6));
} 

/** 
* 
* Method: isCircle(int id1, int id2) 
* 
*/ 
@Test
public void testIsCircle() throws Exception { 
//TODO: Test goes here...
    network.addRelation(1, 2, 10);
    network.addRelation(2, 3, 10);
    network.addRelation(3, 5, 10);
    network.addRelation(5, 100, 10);
    try {
        network.addPerson(new MyPerson(6, "ls", 20));
    } catch (MyEqualPersonIdException e) {
        e.print();
        System.out.println("----12");
    }

    assertTrue(network.isCircle(1, 1));
    assertTrue(network.isCircle(100, 100));
    assertTrue(network.isCircle(5, 5));
    assertTrue(network.isCircle(3, 3));
    assertTrue(network.isCircle(2, 2));

    assertTrue(network.isCircle(1, 2));
    assertTrue(network.isCircle(1, 2));
    assertTrue(network.isCircle(100, 2));
    assertTrue(network.isCircle(5, 3));
    assertTrue(network.isCircle(100, 5));
    assertTrue(network.isCircle(1, 100));
    assertFalse(network.isCircle(1, 6));

    try {
        network.isCircle(1, -1);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----13");
    }
    try {
        network.isCircle(-1, -1);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----14");
    }
    try {
        network.isCircle(-1, 1);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
        System.out.println("----15");
    }
} 

/** 
* 
* Method: queryBlockSum() 
* 
*/ 
@Test
public void testQueryBlockSum() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.queryBlockSum(), 5);
    network.addRelation(1, 2, 10);
    assertEquals(network.queryBlockSum(), 4);
    network.addRelation(2, 3, 10);
    assertEquals(network.queryBlockSum(), 3);
    network.addRelation(3, 5, 10);
    assertEquals(network.queryBlockSum(), 2);
    network.addRelation(5, 100, 10);
    assertEquals(network.queryBlockSum(),1);
} 

/** 
* 
* Method: addGroup(Group group) 
* 
*/ 
@Test
public void testAddGroup() throws Exception { 
//TODO: Test goes here...
    network.addGroup(new MyGroup(100));
} 

/** 
* 
* Method: getGroup(int id) 
* 
*/ 
@Test
public void testGetGroup() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.getGroup(1), group1);
    assertEquals(network.getGroup(2), group2);
    assertNull(network.getGroup(3));
} 

/** 
* 
* Method: addToGroup(int id1, int id2) 
* 
*/ 
@Test
public void testAddToGroup() throws Exception { 
//TODO: Test goes here...
    try {
        network.addToGroup(4, 2);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
    }
    try {
        network.addToGroup(6, 3);
    } catch (MyGroupIdNotFoundException e) {
        e.print();
    }

} 

/** 
* 
* Method: queryGroupSum() 
* 
*/ 
@Test
public void testQueryGroupSum() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.queryGroupSum(), 2);
} 

/** 
* 
* Method: queryGroupPeopleSum(int id) 
* 
*/ 
@Test
public void testQueryGroupPeopleSum() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.queryGroupPeopleSum(1), 2);
    assertEquals(network.queryGroupPeopleSum(2), 2);
    try {
        network.queryGroupPeopleSum(3);
    } catch (MyGroupIdNotFoundException e) {
        e.print();
    }
}

/** 
* 
* Method: queryGroupValueSum(int id) 
* 
*/ 
@Test
public void testQueryGroupValueSum() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.queryGroupValueSum(1), 0);
    assertEquals(network.queryGroupValueSum(2), 0);
    try {
        network.queryGroupValueSum(3);
    } catch (MyGroupIdNotFoundException e){
        e.print();
    }
} 

/** 
* 
* Method: queryGroupAgeMean(int id) 
* 
*/ 
@Test
public void testQueryGroupAgeMean() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.queryGroupValueSum(1), 0);
    assertEquals(network.queryGroupValueSum(2), 0);
    try {
        network.queryGroupValueSum(3);
    } catch (MyGroupIdNotFoundException e){
        e.print();
    }
} 

/** 
* 
* Method: queryGroupAgeVar(int id) 
* 
*/ 
@Test
public void testQueryGroupAgeVar() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.queryGroupValueSum(1), 0);
    assertEquals(network.queryGroupValueSum(2), 0);
    try {
        network.queryGroupValueSum(3);
    } catch (MyGroupIdNotFoundException e){
        e.print();
    }
} 

/** 
* 
* Method: delFromGroup(int id1, int id2) 
* 
*/ 
@Test
public void testDelFromGroup() throws Exception { 
//TODO: Test goes here...
    network.delFromGroup(1, 1);
    try {
        network.delFromGroup(1, 2);
    } catch (MyEqualPersonIdException e) {
        e.print();
    }
    try {
        network.delFromGroup(1, 3);
    } catch (MyGroupIdNotFoundException e) {
        e.print();
    }
    try {
        network.delFromGroup(11, 2);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
    }
} 

/** 
* 
* Method: containsMessage(int id) 
* 
*/ 
@Test
public void testContainsMessage() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: addMessage(Message message) 
* 
*/ 
@Test
public void testAddMessage() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getMessage(int id) 
* 
*/ 
@Test
public void testGetMessage() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: sendMessage(int id) 
* 
*/ 
@Test
public void testSendMessage() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: querySocialValue(int id) 
* 
*/ 
@Test
public void testQuerySocialValue() throws Exception { 
//TODO: Test goes here...
    assertEquals(network.querySocialValue(1), 0);
    try {
        network.querySocialValue(11);
    } catch (MyPersonIdNotFoundException e) {
        e.print();
    }
} 

/** 
* 
* Method: queryReceivedMessages(int id) 
* 
*/ 
@Test
public void testQueryReceivedMessages() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: find(int id) 
* 
*/ 
@Test
public void testFind() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = MyNetwork.getClass().getMethod("find", int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: merge(int id1, int id2) 
* 
*/ 
@Test
public void testMerge() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = MyNetwork.getClass().getMethod("merge", int.class, int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
