/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lession2_oop_strategy.Interface;

/**
 *
 * @author Administrator
 */
public class GoByFlyingFast implements IGoAlgorithm
{
    public void Go()
    {
        System.out.println("Now, i'm Flying Fast! ^^");
    }
}