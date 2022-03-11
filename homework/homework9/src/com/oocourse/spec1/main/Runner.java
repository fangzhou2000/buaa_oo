package com.oocourse.spec1.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;

public class Runner {

    private String[] cmds;
    private Network network;
    private Class<? extends Person> personClass;
    private Class<? extends Network> networkClass;
    private Constructor<? extends Person> personConstructor;
    private Constructor<? extends Network> networkConstructor;
    private Scanner cin;

    public Runner(Class<? extends Person> personClass, Class<? extends Network> networkClass) throws
            NoSuchMethodException, SecurityException {
        this.personClass = personClass;
        this.networkClass = networkClass;
        personConstructor = this.personClass.getConstructor(
                int.class, String.class, int.class);
        networkConstructor = this.networkClass.getConstructor();
        cin = new Scanner(System.in);
    }

    public void run()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        this.network = (Network) this.networkConstructor.newInstance();
        while (cin.hasNextLine()) {
            String cmd = cin.nextLine();
            cmds = cmd.split(" ");
            if (cmds[0].equals("ap")) {
                addPerson();
            } else if (cmds[0].equals("ar")) {
                addRelation();
            } else if (cmds[0].equals("qv")) {
                queryValue();
            } else if (cmds[0].equals("cn")) {
                compareName();
            } else if (cmds[0].equals("qps")) {
                queryPeopleSum();
            } else if (cmds[0].equals("qnr")) {
                queryNameRank();
            } else if (cmds[0].equals("qbs")) {
                queryBlockSum();
            } else if (cmds[0].equals("qci")) {
                queryCircle();
            }
        }
        cin.close();
    }

    private void queryBlockSum() {
        System.out.println(network.queryBlockSum());
    }

    private void queryCircle() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        boolean ret = false;
        try {
            ret = network.isCircle(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        if (ret == true) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
    }

    private void queryNameRank() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryNameRank(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void queryPeopleSum() {
        int ret = network.queryPeopleSum();
        System.out.println(ret);
    }

    private void compareName() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int ret = 0;
        try {
            ret = network.compareName(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        String r;
        if (ret < 0) {
            r = "<";
        } else if (ret == 0) {
            r = "=";
        } else {
            r = ">";
        }
        System.out.println(r);
    }

    private void queryValue() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int ret = 0;
        try {
            ret = network.queryValue(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (RelationNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void addRelation() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int value = Integer.parseInt(cmds[3]);
        try {
            network.addRelation(id1, id2, value);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualRelationException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addPerson()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        int id = Integer.parseInt(cmds[1]);
        String name = cmds[2];
        int age = Integer.parseInt(cmds[3]);
        try {
            network.addPerson((Person) this.personConstructor.newInstance(
                    id, name, age));
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

}
