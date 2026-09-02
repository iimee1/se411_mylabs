package edu.psu.se411.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Stack}.
 *
 * Coverage notes (per lab instructions, methods/branches NOT covered and why):
 * - Stack's public API is only: two constructors, push(E), and pop(). Every
 *   public method and the only branch in pop() (empty vs non-empty) is
 *   exercised below.
 * - There is no getter for the internal ArrayList's capacity, so capacity
 *   handling (default, zero, negative, growth beyond initial capacity) is
 *   tested indirectly through push/pop behavior rather than by inspecting
 *   internal state.
 * - App.java (the Maven-generated entry point) has no logic of its own and
 *   is not under test.
 */
class StackTest {

    private Stack<String> stringStack;

    @BeforeEach
    void setUp() {
        stringStack = new Stack<>();
    }

    // --- Normal / expected behavior ---

    @Test
    void push_then_pop_returns_last_pushed_element() {
        stringStack.push("Z");
        stringStack.push("A");
        assertEquals("A", stringStack.pop());
    }

    @Test
    void pushed_elements_pop_in_reverse_order() {
        stringStack.push("first");
        stringStack.push("second");
        stringStack.push("third");

        assertEquals("third", stringStack.pop());
        assertEquals("second", stringStack.pop());
        assertEquals("first", stringStack.pop());
    }

    // --- Error condition: empty stack ---

    @Test
    void pop_empty_stack() {
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> stringStack.pop(),
                "Expected pop from empty Stack to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().equals("Stack is empty, cannot pop"));
    }

    // --- Boundary cases: constructor / capacity ---

    @Test
    void default_constructor_produces_usable_empty_stack() {
        Stack<Integer> s = new Stack<>();
        assertThrows(NoSuchElementException.class, s::pop);
    }

    @Test
    void zero_or_negative_capacity_still_behaves_like_default() {
        Stack<Integer> zeroCap = new Stack<>(0);
        Stack<Integer> negativeCap = new Stack<>(-5);

        zeroCap.push(1);
        negativeCap.push(2);

        assertEquals(1, zeroCap.pop());
        assertEquals(2, negativeCap.pop());
    }

    @Test
    void pushing_more_elements_than_initial_capacity_still_works() {
        Stack<Integer> smallCap = new Stack<>(2);
        for (int i = 0; i < 50; i++) {
            smallCap.push(i);
        }
        for (int i = 49; i >= 0; i--) {
            assertEquals(i, smallCap.pop());
        }
        assertThrows(NoSuchElementException.class, smallCap::pop);
    }

    // --- Edge case: null values ---

    @Test
    void null_can_be_pushed_and_popped() {
        stringStack.push(null);
        assertNull(stringStack.pop());
    }

    @Test
    void null_mixed_with_real_values_preserves_order() {
        stringStack.push("A");
        stringStack.push(null);
        stringStack.push("B");

        assertEquals("B", stringStack.pop());
        assertNull(stringStack.pop());
        assertEquals("A", stringStack.pop());
    }

    // --- State across multiple calls ---

    @Test
    void interleaved_push_and_pop_keep_correct_state() {
        stringStack.push("A");
        stringStack.push("B");
        assertEquals("B", stringStack.pop());

        stringStack.push("C");
        assertEquals("C", stringStack.pop());
        assertEquals("A", stringStack.pop());

        assertThrows(NoSuchElementException.class, stringStack::pop);
    }

    @Test
    void popping_to_empty_then_pushing_again_works() {
        stringStack.push("A");
        stringStack.pop();
        assertThrows(NoSuchElementException.class, stringStack::pop);

        stringStack.push("B");
        assertEquals("B", stringStack.pop());
    }

    @Test
    void two_stack_instances_do_not_share_state() {
        Stack<String> other = new Stack<>();
        stringStack.push("mine");
        other.push("other's");

        assertEquals("mine", stringStack.pop());
        assertEquals("other's", other.pop());
    }
}
