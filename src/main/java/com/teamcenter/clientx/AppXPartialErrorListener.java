//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.clientx;

import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ErrorValue;
import com.teamcenter.soa.client.model.PartialErrorListener;

/**
 * Implementation of the PartialErrorListener. Print out any partial errors
 * returned.
 *
 */
public class AppXPartialErrorListener implements PartialErrorListener
{

    @Override
    public void handlePartialError(ErrorStack[] stacks)
    {
        if (stacks.length == 0) return;

        System.out.println("");
        System.out.println("*****");
        System.out.println("Partial Errors caught in com.teamcenter.clientx.AppXPartialErrorListener.");


        for (int i = 0; i < stacks.length; i++)
        {
            ErrorValue[] errors = stacks[i].getErrorValues();
            System.out.print("Partial Error for ");

            // The different service implementation may optionally associate
            // an ModelObject, client ID, or nothing, with each partial error
            if (stacks[i].hasAssociatedObject())
            {
                System.out.println( "object " + stacks[i].getAssociatedObject().getUid()  );
            }
            else if (stacks[i].hasClientId())
            {
                System.out.println( "client id " + stacks[i].getClientId()  );
            }
            else if (stacks[i].hasClientIndex())
                System.out.println( "client index " + stacks[i].getClientIndex()  );


            // Each Partial Error will have one or more contributing error messages
            for (int j = 0; j < errors.length; j++)
            {
                System.out.println("    Code: " + errors[j].getCode() + "\tSeverity: "
                        + errors[j].getLevel() + "\t" + errors[j].getMessage());
            }
        }

    }

}
