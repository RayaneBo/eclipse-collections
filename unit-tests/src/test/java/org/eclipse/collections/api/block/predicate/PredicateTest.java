/*
 * Copyright (c) 2021 The Bank of New York Mellon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.block.predicate;

import org.eclipse.collections.impl.block.factory.Predicates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredicateTest
{
    @Test
    public void testBasicPredicates()
    {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.test(Boolean.TRUE));
        assertTrue(alwaysTrue.test(Boolean.FALSE));

        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.test(Boolean.TRUE));
        assertFalse(alwaysFalse.test(Boolean.FALSE));
    }

    @Test
    public void testNullHandling()
    {
        Predicate<Object> isNull = Predicates.isNull();
        assertTrue(isNull.test(null));
        assertFalse(isNull.test(new Object()));

        Predicate<Object> notNull = Predicates.notNull();
        assertFalse(notNull.test(null));
        assertTrue(notNull.test(new Object()));
    }

    @Test
    public void testInstanceOf()
    {
        Predicate<Object> isInteger = Predicates.instanceOf(Integer.class);
        assertTrue(isInteger.test(1));
        assertFalse(isInteger.test("1"));
        assertFalse(isInteger.test(null));
    }

    @Test
    public void testNotInstanceOf()
    {
        Predicate<Object> notInteger = Predicates.notInstanceOf(Integer.class);
        assertFalse(notInteger.test(1));
        assertTrue(notInteger.test("1"));
        assertTrue(notInteger.test(null));
    }

    @Test
    public void testEqual()
    {
        Predicate<Object> equalsOne = Predicates.equal(1);
        assertTrue(equalsOne.test(1));
        assertFalse(equalsOne.test(2));
        assertFalse(equalsOne.test(null));
    }

    @Test
    public void testNotEqual()
    {
        Predicate<Object> notEqualsOne = Predicates.notEqual(1);
        assertFalse(notEqualsOne.test(1));
        assertTrue(notEqualsOne.test(2));
        assertTrue(notEqualsOne.test(null));
    }

    @Test
    public void testAnd()
    {
        Predicate<Object> isIntegerAndPositive = Predicates.and(
            Predicates.instanceOf(Integer.class),
            Predicates.greaterThan(0)
        );
        assertTrue(isIntegerAndPositive.test(1));
        assertFalse(isIntegerAndPositive.test(-1));
        assertFalse(isIntegerAndPositive.test("1"));
    }

    @Test
    public void testOr()
    {
        Predicate<Object> isIntegerOrString = Predicates.or(
            Predicates.instanceOf(Integer.class),
            Predicates.instanceOf(String.class)
        );
        assertTrue(isIntegerOrString.test(1));
        assertTrue(isIntegerOrString.test("1"));
        assertFalse(isIntegerOrString.test(1.0));
    }

    @Test
    public void testNot()
    {
        Predicate<Object> notIsInteger = Predicates.not(Predicates.instanceOf(Integer.class));
        assertFalse(notIsInteger.test(1));
        assertTrue(notIsInteger.test("1"));
        assertTrue(notIsInteger.test(null));
    }

    @Test
    public void testAttributePredicate()
    {
        Predicate<String> lengthGreaterThanTwo = Predicates.attributeGreaterThan(String::length, 2);
        assertTrue(lengthGreaterThanTwo.test("abc"));
        assertFalse(lengthGreaterThanTwo.test("ab"));
    }

    @Test
    public void testIn()
    {
        Predicate<Object> inSet = Predicates.in(Predicates.alwaysTrue(), Predicates.alwaysFalse());
        assertTrue(inSet.test(Predicates.alwaysTrue()));
        assertTrue(inSet.test(Predicates.alwaysFalse()));
        assertFalse(inSet.test(Predicates.isNull()));
    }

    @Test
    public void testNotIn()
    {
        Predicate<Object> notInSet = Predicates.notIn(Predicates.alwaysTrue(), Predicates.alwaysFalse());
        assertFalse(notInSet.test(Predicates.alwaysTrue()));
        assertFalse(notInSet.test(Predicates.alwaysFalse()));
        assertTrue(notInSet.test(Predicates.isNull()));
    }

    @Test
    public void testBetweenInclusive()
    {
        Predicate<Integer> betweenOneAndThree = Predicates.betweenInclusive(1, 3);
        assertTrue(betweenOneAndThree.test(1));
        assertTrue(betweenOneAndThree.test(2));
        assertTrue(betweenOneAndThree.test(3));
        assertFalse(betweenOneAndThree.test(0));
        assertFalse(betweenOneAndThree.test(4));
    }

    @Test
    public void testGreaterThan()
    {
        Predicate<Integer> greaterThanOne = Predicates.greaterThan(1);
        assertTrue(greaterThanOne.test(2));
        assertFalse(greaterThanOne.test(1));
        assertFalse(greaterThanOne.test(0));
    }

    @Test
    public void testLessThan()
    {
        Predicate<Integer> lessThanOne = Predicates.lessThan(1);
        assertTrue(lessThanOne.test(0));
        assertFalse(lessThanOne.test(1));
        assertFalse(lessThanOne.test(2));
    }

    @Test
    public void testGreaterThanOrEqualTo()
    {
        Predicate<Integer> greaterThanOrEqualToOne = Predicates.greaterThanOrEqualTo(1);
        assertTrue(greaterThanOrEqualToOne.test(1));
        assertTrue(greaterThanOrEqualToOne.test(2));
        assertFalse(greaterThanOrEqualToOne.test(0));
    }

    @Test
    public void testLessThanOrEqualTo()
    {
        Predicate<Integer> lessThanOrEqualToOne = Predicates.lessThanOrEqualTo(1);
        assertTrue(lessThanOrEqualToOne.test(1));
        assertTrue(lessThanOrEqualToOne.test(0));
        assertFalse(lessThanOrEqualToOne.test(2));
    }
}
