package db61b;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

/** @author Yuki Mizuno
 */
/** Basic tests for the database.
 */
public class Tests {
    @Test
    public void testBasicRow() {
        Row row = new Row(new String[]{"Hello", "world."});
        assertEquals(2, row.size());
    }
    @Test
    public void testColumnIndex() {
        Table t = new Table("table", new String[]{"Hello ", "world"});
        assertEquals(1, t.columnIndex("world"));
    }
    @Test
    public void testTableSizeAndRowAdd() {
        Table t = new Table("t", new String[]{"Hello", "world."});
        assertEquals(0, t.size());
        Row r = new Row(new String[] {"Goodbye", "world"});
        t.add(r);
        assertEquals(1, t.size());
        t.print();
    }
    @Test
    public void testReadTable() {
        Table schedule = Table.readTable("enrolled");
        assertEquals(3, schedule.numColumns());
        assertEquals(19, schedule.size());
        schedule.print();
    }
    @Test
    public void testAddAndRowSize() {
        HashSet<Row> rows = new HashSet<Row>();
        Row row1 = new Row(new String[]{"Hello", "world."});
        Row row2 = new Row(new String[]{"Good", "Bye", "World."});
        rows.add(row1);
        rows.add(row2);
        assertEquals(2, rows.size());
        assertEquals(true, rows.contains(row2));
    }
    @Test
    public void testTableIterator() {
        Table schedule = Table.readTable("enrolled");
        TableIterator schediter = new TableIterator(schedule);
        assertEquals(true, schediter.hasRow());
        Row next = schediter.next();
        Row next2 = schediter.next();
        assertEquals(true, schediter.hasRow());
        Table t = new Table("newSched", new String[]{"none"});
        t.add(next);
        t.add(next2);
        t.print();
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Tests.class));
    }
}
