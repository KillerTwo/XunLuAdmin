package org.wm.generator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wm.generator.domain.SysMenu;
import org.wm.generator.mapper.SysMenuMapper;

import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootTest
class GeneratorApplicationTests {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    private Object object;

    @Test
    void contextLoads() {

        var menu = new SysMenu();
        menu.setName("BackAuthBtn");
        menu.setPath("btn");
        menu.setComponent("/demo/permission/back/Btn");
        menu.setRedirect("");
        menu.setParentId(5L);

        var meta = new SysMenu.Meta();
        meta.setCurrentActiveMenu("");
        meta.setIcon("carbon:user-role");
        meta.setTitle("routes.demo.permission.backBtn");
        meta.setHideBreadcrumb(false);
        meta.setHideMenu(false);


        menu.setMeta(meta);
        sysMenuMapper.insertSysMenu(menu);

    }

    public static void main(String[] args) {
        /*int i = 10 << 16;
        System.err.println(i);*/
        /*Node<Integer> node = new Node<>();
        node.setF("1");

        Node<Integer> node1 = new Node<>();
        node.setF("2");

        node.appendRelaxed(node = node1);*/
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        queue.offer(1);

        queue.poll();

        queue.poll();

    }

    static final class Node<E> {
        volatile E item;
        String f;

        Node<E> node;

        Node() {}

        void appendRelaxed(Node<E> node) {
            // assert next != null;
            // assert this.next == null;
            this.node = node;
            System.err.println(this.item);
        }

        public E getItem() {
            return item;
        }

        public void setItem(E item) {
            this.item = item;
        }

        public String getF() {
            return f;
        }

        public void setF(String f) {
            this.f = f;
        }
    }

}
