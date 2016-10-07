package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.task.*;

/**
 *
 */
public class TypicalTestPersons {

    public static TestTask alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalTestPersons() {
        try {
            alice =  new TaskBuilder().withName("Do CS2103 Pretut").withAddress("At Home")
                    .withEndDate("07102016").withStartDate("01102016")
                    .withTags("Priority").build();
            benson = new TaskBuilder().withName("Do CS2103 Project").withAddress("At School")
                    .withEndDate("14102016").withStartDate("07102016")
                    .withTags("Overdue", "Priority").build();
            carl = new TaskBuilder().withName("Eat Mcdonalds").withAddress("At Technoedge")
                    .withEndDate("21102016").withStartDate("14102016")
                    .withTags("Priority", "Now").build();
            daniel = new TaskBuilder().withName("Flunk CS2103").withAddress("In the exam hall")
                    .withEndDate("21112016").withStartDate("21112016")
                    .withTags("Now", "Priority").build();
            elle = new TaskBuilder().withName("Working at Mcdonalds").withAddress("At Mcdonalds")
                    .withEndDate("31122047").withStartDate("01042017")
                    .withTags().build();
            fiona = new TaskBuilder().withName("Send kids to NUS").withAddress("At NUS")
                    .withEndDate("01042051").withStartDate("01082047")
                    .withTags().build();
            elle = new TaskBuilder().withName("Make kids study CS2103").withAddress("At ICube Lecture Hall")
                    .withEndDate("01122050").withStartDate("01082049")
                    .withTags().build();
            george = new TaskBuilder().withName("Make kids work at Mcdonalds").withAddress("At the same workplace")
                    .withEndDate("02042091").withStartDate("02042051")
                    .withTags().build();
            //Manually added
            hoon = new TaskBuilder().withName("Regret working at Mcdonalds").withAddress("At the hospital")
                    .withEndDate("03042052").withStartDate("02042052")
                    .withTags().build();
            ida = new TaskBuilder().withName("Thinking about what happen if I fail CS2103").withAddress("At ICube Lecture Hall")
                    .withEndDate("07102016").withStartDate("07102016")
                    .withTags().build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(AddressBook ab) {

        try {
            ab.addPerson(new Task(alice));
            ab.addPerson(new Task(benson));
            ab.addPerson(new Task(carl));
            ab.addPerson(new Task(daniel));
            ab.addPerson(new Task(elle));
            ab.addPerson(new Task(fiona));
            ab.addPerson(new Task(george));
        } catch (UniquePersonList.DuplicatePersonException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalPersons() {
        return new TestTask[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public AddressBook getTypicalAddressBook(){
        AddressBook ab = new AddressBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}
