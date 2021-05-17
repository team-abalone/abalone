package com.teamabalone.abalone.logic;

import com.teamabalone.abalone.Gamelogic.Directions;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.HexCoordinate;
import com.teamabalone.abalone.Gamelogic.Marble;
import com.teamabalone.abalone.Gamelogic.Team;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class testMove {
    Field field = new Field(5);


    /*
            #   #   #   #   #               5
          #   #   #   #   #   #             6
        #   #   X   X   X   #   #           7
      #   #   #   #   #   #   #   #         8
        Field: 14, 15, 16
     */


    public void setUpWithThree(){
        cleanField();
        for (HexCoordinate hex : field.iterateOverHexagons()) {
            if (field.getHexagon(hex).getId() >= 14 && field.getHexagon(hex).getId() <= 16) {
                field.getHexagon(hex).setMarble(new Marble(Team.WHITE));
            }
        }
    }

    @After
    public void cleanField(){
        for (HexCoordinate hex : field.iterateOverHexagons()) {
            if(field.getHexagon(hex).getMarble() != null){
                field.getHexagon(hex).setMarble(null);
            }
        }
        field.setGotPushedOut();
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

    public Field cleanDefaultField(Field test){
        for (HexCoordinate hex : test.iterateOverHexagons()) {
            if(test.getHexagon(hex).getMarble() != null){
                test.getHexagon(hex).setMarble(null);
            }
        }
        return test;
    }

    @Test
    public void moveDefault(){
        // #   #   X   X   X   #   #           7
        setUpWithThree();
        Field wanted = setUpAsWanted(14,15,16);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveLineOut(){
        // #   #   #   #   #   X   X           7
        Field wanted = setUpAsWanted( 17, 18);
        Field field = setUpAsWanted(  16,17, 18);
        field.move(new int[]{16, 17, 18}, Directions.RIGHT);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
        Assert.assertTrue(field.isPushedOutOfBound());
    }

    @Test
    public void moveLineRight(){
        // #   #   #   X   X   X   #           7
        setUpWithThree();
        Field wanted = setUpAsWanted( 15, 16,17);
        field.move(new int[]{14, 15, 16}, Directions.RIGHT);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveLineLeft(){
        // #   X   X   X   #    #   #           7
        setUpWithThree();
        Field wanted = setUpAsWanted( 13, 14, 15);
        field.move(new int[]{14, 15, 16}, Directions.LEFT);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveLineRightUp(){
        //  #   #   X   X   X   #             6
        //#   #   #   #   #   #   #           7
        setUpWithThree();
        Field wanted = setUpAsWanted( 8,9,10);
        field.move(new int[]{14, 15, 16}, Directions.RIGHTUP);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveLineLeftUp(){
        //  #   X   X   X   #   #             6
        //#   #   #   #   #   #   #           7
        setUpWithThree();
        Field wanted = setUpAsWanted( 7,8,9);
        field.move(new int[]{14, 15, 16}, Directions.LEFTUP);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveLineLeftDown(){
        //    #   #   #   #   #   #   #           7
        //  #   #   X   X   X   #   #   #         8
        setUpWithThree();
        Field wanted = setUpAsWanted( 21,22,23);
        field.move(new int[]{14, 15, 16}, Directions.LEFTDOWN);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveLineRightDown(){
        //    #   #   #   #   #   #   #           7
        //  #   #   #   X   X   X   #   #         8
        setUpWithThree();
        Field wanted = setUpAsWanted( 22,23,24);
        field.move(new int[]{14, 15, 16}, Directions.RIGHTDOWN);
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToRight_Out(){
        //            #   #   X   #   #               5
        //          #   #   X   #   #   #             6
        //        #   #   #   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 3,8);
        field = setUpAsWanted(3,8, 14);
        field.move(new int[]{3,8,14}, Directions.RIGHTUP);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
        Assert.assertTrue(field.isPushedOutOfBound());
    }

    @Test
    public void moveTiltedToRight_Right(){
        //            #   #   #   X   #               5
        //          #   #   #   X   #   #             6
        //        #   #   #   X   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 4,9,15);
        field = setUpAsWanted(3,8, 14);
        field.move(new int[]{3,8,14}, Directions.RIGHT);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToRight_Left(){
        //            #   X   #   #   #               5
        //          #   X   #   #   #   #             6
        //        #   X   #   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 2,7,13);
        field = setUpAsWanted(3,8, 14);
        field.move(new int[]{3,8,14}, Directions.LEFT);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToRight_RightDown(){
        //            #   #   #   #   #               5
        //          #   #   #   X   #   #             6
        //        #   #   #   X   #   #   #           7
        //      #   #   #   X   #   #   #   #         8
        Field wanted = setUpAsWanted( 9,15,22);
        field = setUpAsWanted(3,8, 14);
        field.move(new int[]{3,8,14}, Directions.RIGHTDOWN);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToRight_LeftDown(){
        //            #   #   #   #   #               5
        //          #   #   X   #   #   #             6
        //        #   #   X   #   #   #   #           7
        //      #   #   X   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 8,14,21);
        field = setUpAsWanted(3,8, 14);
        field.move(new int[]{3,8,14}, Directions.LEFTDOWN);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToRight_RightUp(){
        //            #   #   X   #   #               5
        //          #   #   X   #   #   #             6
        //        #   #   X   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 3,8,14);
        field = setUpAsWanted(8,14, 21);
        field.move(new int[]{8,14,21}, Directions.RIGHTUP);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToRight_LeftUp(){
        //            #   X   #   #   #               5
        //          #   X   #   #   #   #             6
        //        #   X   #   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 2,7,13);
        field = setUpAsWanted(8,14, 21);
        field.move(new int[]{8,14,21}, Directions.LEFTUP);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_Out(){
        //            #   #   #   #   #               5
        //          #   #   #   #   #   #             6
        //        #   #   #   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        //    #   #   #   #   #   #   #   #   #       9
        //      #   #   #   #   #   #   #   #         8
        //        #   #   #   #   #   #   #           7
        //          #   #   #   X   #   #             6
        //            #   #   #   X   #               5
        Field wanted = setUpAsWanted( 54,60);
        field = setUpAsWanted(47, 54, 60);
        printArrayAsField(field.getMarbles());

        field.move(new int[]{47, 54, 60}, Directions.RIGHTDOWN);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_Right(){
        //            #   #   X   #   #               5
        //          #   #   #   X   #   #             6
        //        #   #   #   #   X   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 3,9,16);
        field = setUpAsWanted(2,8, 15);
        field.move(new int[]{2,8,15}, Directions.RIGHT);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_Left(){
        //            X   #   #   #   #               5
        //          #   X   #   #   #   #             6
        //        #   #   X   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 1,7,14);
        field = setUpAsWanted(2,8, 15);
        field.move(new int[]{2,8,15}, Directions.LEFT);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_RightDown(){
        //            #   #   #   #   #               5
        //          #   #   X   #   #   #             6
        //        #   #   #   X   #   #   #           7
        //      #   #   #   #   X   #   #   #         8
        Field wanted = setUpAsWanted( 8,15,23);
        field = setUpAsWanted(2,8, 15);
        field.move(new int[]{2,8,15}, Directions.RIGHTDOWN);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_LeftDown(){
        //            #   #   #   #   #               5
        //          #   X   #   #   #   #             6
        //        #   #   X   #   #   #   #           7
        //      #   #   #   X   #   #   #   #         8
        Field wanted = setUpAsWanted( 7,14,22);
        field = setUpAsWanted(2,8, 15);
        field.move(new int[]{2,8,15}, Directions.LEFTDOWN);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_RightUp(){
        //            #   X   #   #   #               5
        //          #   #   X   #   #   #             6
        //        #   #   #   X   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 2,8,15);
        field = setUpAsWanted(7,14, 22);
        field.move(new int[]{7,14,22}, Directions.RIGHTUP);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    @Test
    public void moveTiltedToLeft_LeftUp(){
        //            #   #   #   #   #               5
        //          #   X   #   #   #   #             6
        //        #   #   X   #   #   #   #           7
        //      #   #   #   #   #   #   #   #         8
        Field wanted = setUpAsWanted( 1,7,14);
        field = setUpAsWanted(7,14, 22);
        field.move(new int[]{7,14,22}, Directions.LEFTUP);
        printArrayAsField(wanted.getMarbles());
        printArrayAsField(field.getMarbles());
        Assert.assertEquals(wanted.getMarbles(), field.getMarbles());
    }

    //hardcoded
    public void printArrayAsField(List<Team> array){
        System.out.print("            ");
        for (int i = 0; i < 5 ; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("         ");
        for (int i = 5; i < 11; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("      ");
        for (int i = 11; i < 18; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("    \n");
        System.out.print("   ");
        for (int i = 18; i < 26; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print(" \n");
        for (int i = 26; i < 35; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("  \n");
        System.out.print("   ");
        for (int i = 35; i < 43; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("      ");
        for (int i = 43; i < 50; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("      \n");
        System.out.print("         ");
        for (int i = 50; i < 56; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }System.out.print("          \n");
        System.out.print("            ");
        for (int i = 56; i < 61 ; i++) {
            if(array.get(i) == null){
                System.out.print("#"+ "    ");
            }else{
                System.out.print("X"+ "    ");
            }
        }
        System.out.print("\n\n");
    }
}
