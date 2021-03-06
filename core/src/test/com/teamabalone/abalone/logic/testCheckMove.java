package com.teamabalone.abalone.logic;

import com.teamabalone.abalone.Gamelogic.Directions;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.HexCoordinate;
import com.teamabalone.abalone.Gamelogic.Marble;
import com.teamabalone.abalone.Gamelogic.Team;

import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class testCheckMove {

    Field field = new Field(5);
    //            #   #   #   #   #               5
    //          #   #   #   #   #   #             6
    //        #   #   #   #   #   #   #           7
    //      #   #   #   #   #   #   #   #         8
    //    #   #   #   #   #   #   #   #   #       9
    //      #   #   #   #   #   #   #   #         8
    //        #   #   #   #   #   #   #           7
    //          #   #   #   #   #   #             6
    //            #   #   #   #   #               5



    @After
    public void cleanField(){
        for (HexCoordinate hex : field.iterateOverHexagons()) {
            if(field.getHexagon(hex).getMarble() != null){
                field.getHexagon(hex).setMarble(null);
            }
        }
        field.setGotPushedOut();
    }

    public Field setUpAsWanted(int one, int two ,int three, int four){
        Field test = new Field(5);
        test = cleanDefaultField(test);
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one || test.getHexagon(hex).getId() == two || test.getHexagon(hex).getId() == three || test.getHexagon(hex).getId() == four) {
                test.getHexagon(hex).setMarble(new Marble(Team.WHITE));
            }
        }
        return test;
    }

    public Field setUpAsWanted(int one, int two ,int three){
        Field test = new Field(5);
        test = cleanDefaultField(test);
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one || test.getHexagon(hex).getId() == two || test.getHexagon(hex).getId() == three ) {
                test.getHexagon(hex).setMarble(new Marble(Team.WHITE));
            }
        }
        return test;
    }

    public Field setUpAsWanted(int one, int two){
        Field test = new Field(5);
        test = cleanDefaultField(test);
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one || test.getHexagon(hex).getId() == two) {
                test.getHexagon(hex).setMarble(new Marble(Team.WHITE));
            }
        }
        return test;
    }

    public Field setUpAsWanted(int one){
        Field test = new Field(5);
        test = cleanDefaultField(test);
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one) {
                test.getHexagon(hex).setMarble(new Marble(Team.WHITE));
            }
        }
        return test;
    }

    public Field setUpEnemyAsWanted(Field test, int one, int two ,int three){
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one || test.getHexagon(hex).getId() == two || test.getHexagon(hex).getId() == three ) {
                test.getHexagon(hex).setMarble(new Marble(Team.BLACK));
            }
        }
        return test;
    }

    public Field setUpEnemyAsWanted(Field test, int one, int two){
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one || test.getHexagon(hex).getId() == two) {
                test.getHexagon(hex).setMarble(new Marble(Team.BLACK));
            }
        }
        return test;
    }

    public Field setUpEnemyAsWanted(Field test, int one){
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one) {
                test.getHexagon(hex).setMarble(new Marble(Team.BLACK));
            }
        }
        return test;
    }

    public Field setUpEnemyAsWanted(Field test, int one, int two ,int three, int four){
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if (test.getHexagon(hex).getId() == one || test.getHexagon(hex).getId() == two || test.getHexagon(hex).getId() == three || test.getHexagon(hex).getId() == four) {
                test.getHexagon(hex).setMarble(new Marble(Team.BLACK));
            }
        }
        return test;
    }

    public Field cleanDefaultField(Field test){
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if(test.getHexagon(hex).getMarble() != null){
                test.getHexagon(hex).setMarble(null);
            }
        }
        test.setGotPushedOut();
        return test;
    }

    public void printArrayAsField(List<Team> array){
        System.out.print("            ");
        for (int i = 0; i < 5 ; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("         ");
        for (int i = 5; i < 11; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("      ");
        for (int i = 11; i < 18; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("    \n");
        System.out.print("   ");
        for (int i = 18; i < 26; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print(" \n");
        for (int i = 26; i < 35; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("  \n");
        System.out.print("   ");
        for (int i = 35; i < 43; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("      ");
        for (int i = 43; i < 50; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("         ");
        for (int i = 50; i < 56; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }System.out.print("          \n");
        System.out.print("            ");
        for (int i = 56; i < 61 ; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else if(array.get(i) == Team.BLACK){
                System.out.print("B" + "    ");
            }else {
                System.out.print("W"+ "    ");
            }
        }
        System.out.print("\n\n");
    }


    @Test
    public void normalMove(){

        Field wanted = setUpAsWanted(15,16, 17);
        field = setUpAsWanted(14, 15, 16);
        int[] answer = field.checkMove(new int[]{14, 15, 16}, Directions.RIGHT);
        assertEquals(wanted.getMarbles(), field.getMarbles());
        assertEquals(0, answer.length);
    }

    @Test
    public void normalMoveTiltedLeft(){
        Field wanted = setUpAsWanted(7, 14, 22);
        field = setUpAsWanted(1, 7, 14);
        int[] answer = field.checkMove(new int[]{1, 7, 14}, Directions.RIGHTDOWN);
        assertEquals(wanted.getMarbles(), field.getMarbles());
        assertEquals(0, answer.length);
    }

    @Test
    public void normalMoveTiltedRight(){
        Field wanted = setUpAsWanted(9, 15, 22);
        field = setUpAsWanted(3, 8, 14);
        int[] answer = field.checkMove(new int[]{3, 8, 14}, Directions.RIGHTDOWN);
        assertEquals(wanted.getMarbles(), field.getMarbles());
        assertEquals(0, answer.length);
    }

    @Test
    public void moveOutOfBound(){
        field = setUpAsWanted(3,4,5);
        assertNull(field.checkMove(new int[]{3, 4, 5}, Directions.RIGHT));
    }

    @Test
    public void moveOutOfBoundTwo(){
        field = setUpAsWanted(1,7,14);
        assertNull(field.checkMove(new int[]{1, 7, 14}, Directions.LEFTUP));
    }

    @Test
    public void moveIntoOneEnemyWithOne(){
        field = setUpAsWanted(15);
        field = setUpEnemyAsWanted(field,16);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{15}, Directions.RIGHT));
    }

    @Test
    public void moveIntoMoreEnemyWithOne(){
        field = setUpAsWanted(15);
        field = setUpEnemyAsWanted(field,16, 17);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{15}, Directions.RIGHT));
    }

    @Test
    public void moveIntoOneEnemyWithTwo(){
        Field wanted = setUpAsWanted(15,16);
        wanted = setUpEnemyAsWanted(wanted, 17);
        field = setUpAsWanted(14,15);
        field = setUpEnemyAsWanted(field,16);
        printArrayAsField(field.getMarbles());
        int[] result = field.checkMove(new int[]{14, 15}, Directions.RIGHT);
        assertArrayEquals(new int[]{16}, result);
        assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveIntoTwoEnemyWithTwo(){
        field = setUpAsWanted(14,15);
        field = setUpEnemyAsWanted(field,16, 17);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14, 15}, Directions.RIGHT));
    }

    @Test
    public void moveIntoOneEnemyWithThree(){
        Field wanted = setUpAsWanted(15,16, 17);
        wanted = setUpEnemyAsWanted(wanted, 18);
        field = setUpAsWanted(14,15, 16);
        field = setUpEnemyAsWanted(field,17);
        printArrayAsField(field.getMarbles());
        int[] result = field.checkMove(new int[]{14, 15, 16}, Directions.RIGHT);
        assertArrayEquals(new int[]{17}, result);
        assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveIntoTwoEnemyWithThree(){
        Field wanted = setUpAsWanted(14, 15,16 );
        wanted = setUpEnemyAsWanted(wanted, 17, 18);
        field = setUpAsWanted(13,14,15 );
        field = setUpEnemyAsWanted(field,16, 17);
        printArrayAsField(field.getMarbles());
        int[] result = field.checkMove(new int[]{13 ,14, 15}, Directions.RIGHT);
        assertArrayEquals(new int[]{16, 17}, result);
        assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveIntoThreeEnemyWithThree(){
        field = setUpAsWanted(13, 14,15);
        field = setUpEnemyAsWanted(field,16, 17, 18);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{13, 14, 15}, Directions.RIGHT));
    }

    @Test
    public void moveIntoMoreEnemyWithThree(){
        field = setUpAsWanted(1, 7,14);
        field = setUpEnemyAsWanted(field,22, 31, 40, 48);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{1, 7, 14}, Directions.RIGHTDOWN));
    }

    @Test
    public void moveOneEnemyOutWithTwo(){
        Field wanted = setUpAsWanted(16,17 ,18 );
        field = setUpAsWanted(15, 16, 17);
        field = setUpEnemyAsWanted(field,18);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        int[] result = field.checkMove(new int[]{15, 16, 17}, Directions.RIGHT);
        printArrayAsField(field.getMarbles());
        assertArrayEquals(new int[]{18}, result);
        assertEquals(wanted.getMarbles(), field.getMarbles());
        assertTrue(field.isPushedOutOfBound());
    }

    @Test
    public void moveOneEnemyOutWithThree(){
        Field wanted = setUpAsWanted(1,7 ,14 );
        field = setUpAsWanted(7, 14, 22);
        field = setUpEnemyAsWanted(field,1);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        int[] result = field.checkMove(new int[]{7, 14, 22}, Directions.LEFTUP);
        printArrayAsField(field.getMarbles());
        assertArrayEquals(new int[]{1}, result);
        assertEquals(wanted.getMarbles(), field.getMarbles());
        assertTrue(field.isPushedOutOfBound());
    }

    @Test
    public void moveTwoEnemyOutWithThree(){
        Field wanted = setUpAsWanted(6,12 ,19 );
        wanted = setUpEnemyAsWanted(wanted, 27);
        field = setUpAsWanted(1, 6, 12);
        field = setUpEnemyAsWanted(field,19,27);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        int[] result = field.checkMove(new int[]{1, 6, 12}, Directions.LEFTDOWN);
        printArrayAsField(field.getMarbles());
        assertArrayEquals(new int[]{19, 27}, result);
        assertEquals(wanted.getMarbles(), field.getMarbles());
        assertTrue(field.isPushedOutOfBound());
    }

    @Test
    public void moveOneAllyIntoAlly(){
        field = setUpAsWanted(14, 15);
        assertNull(field.checkMove(new int[]{15}, Directions.LEFT));
    }

    @Test
    public void moveTwoAllyIntoAlly(){
        field = setUpAsWanted(1, 7, 14);
        assertNull(field.checkMove(new int[]{1, 6}, Directions.RIGHTDOWN));
    }

    @Test
    public void moveThreeAllyIntoAlly(){
        field = setUpAsWanted(1, 6, 12, 19);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{6, 12, 19}, Directions.RIGHTUP));
    }

    @Test
    public void moveLineAllyIntoAllyLeft(){
        field = setUpAsWanted(14,15,16, 21);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14, 15, 16}, Directions.LEFTDOWN));
    }

    @Test
    public void moveLineAllyIntoAllyMid(){
        field = setUpAsWanted(14,15,16, 9);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14, 15, 16}, Directions.RIGHTUP));
    }

    @Test
    public void moveLineAllyIntoAllyRight(){
        field = setUpAsWanted(14,15,16, 10);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14, 15, 16}, Directions.RIGHTUP));
    }

    @Test
    public void moveLineAllyIntoAllyTilted(){
        field = setUpAsWanted(8,15,23, 16);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{8,15,23}, Directions.RIGHT));
    }

    @Test
    public void moveLineAllyIntoEnemyTiltOne(){
        field = setUpAsWanted(8,15,23);
        field = setUpEnemyAsWanted(field, 16);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{8,15,23}, Directions.RIGHT));
    }

    @Test
    public void moveLineAllyIntoEnemyTiltTwo(){
        field = setUpAsWanted(8,15,23);
        field = setUpEnemyAsWanted(field, 22);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{8,15,23}, Directions.LEFT));
    }

    @Test
    public void moveLineAllyIntoEnemyTiltTHree(){
        field = setUpAsWanted(8,15,23);
        field = setUpEnemyAsWanted(field, 7,14,22);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{8,15,23}, Directions.LEFT));
    }

    @Test
    public void moveLineAllyIntoEnemyTiltFour(){
        field = setUpAsWanted(8,15,23);
        field = setUpEnemyAsWanted(field,  14, 22, 31);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{8,15,23}, Directions.LEFT));
    }

    @Test
    public void moveLineAllyIntoEnemyStraightOne(){
        field = setUpAsWanted(14,15,16);
        field = setUpEnemyAsWanted(field, 22);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14,15,16}, Directions.RIGHTDOWN));
    }

    @Test
    public void moveLineAllyIntoEnemyStraightOneOther(){
        field = setUpAsWanted(14,15,16);
        field = setUpEnemyAsWanted(field, 24);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14,15,16}, Directions.RIGHTDOWN));
    }

    @Test
    public void moveLineAllyIntoEnemyStraightTwo(){
        field = setUpAsWanted(14,15,16);
        field = setUpEnemyAsWanted(field, 8);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14,15,16}, Directions.LEFTUP));
    }

    @Test
    public void moveLineAllyIntoEnemyStraightThree(){
        field = setUpAsWanted(14,15,16);
        field = setUpEnemyAsWanted(field, 21, 22,23);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14,15,16}, Directions.RIGHTDOWN));
    }

    @Test
    public void moveLineAllyIntoEnemyStraightFour(){
        field = setUpAsWanted(14,15,16);
        field = setUpEnemyAsWanted(field, 21, 22,23);
        printArrayAsField(field.getMarbles());
        assertNull(field.checkMove(new int[]{14,15,16}, Directions.LEFTDOWN));
    }







}
