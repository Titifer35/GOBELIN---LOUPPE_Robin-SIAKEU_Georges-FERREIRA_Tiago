    # Import PuLP modeler functions
from pulp import *
import json
import sys


if __name__ == "__main__":

    if(len(sys.argv) < 3):
        print("Please, provide the file to the input data object, and the output data file path/name")
        exit(1)
        
    inputfilename = sys.argv[1]
    outputfilename = sys.argv[2]


    # Opening JSON file
    with open("Donnees.json") as openfile:
        # Reading from json file
        data = json.load(openfile)


    # Create the sets
    set_I = list(range(0, data["num_customers"], 1))# Set of clients
    set_J = list(range(0, data["num_facility_locations"],1))  # Set of facilities


    # Create the decision variables
    x_var = LpVariable.dicts('x', (set_I, set_J), 0, 1, cat="Binary")
    y_var = LpVariable.dicts('y', set_J, 0, 1, cat="Binary")


    # Create the 'SSCFLP' variable to contain the problem data
    SSCFLP = LpProblem("SSCFLP", LpMinimize)

    # The objective function is added to 'problem3' first
    SSCFLP += lpSum(data['cost_matrix'][j][i]* x_var[i][j] for i in set_I for j in set_J) + \
              lpSum(data['fixed_cost_facility'][j] * y_var[j] for j in set_J)


    # Capacity constraint for each location j
    for j in set_J:
        SSCFLP += lpSum(data['demand_customer'][i] * x_var[i][j] for i in set_I) <= data['capacity_facility'][j] * y_var[j]

    # Constraint for the demand satisfaction of each client i
    for i in set_I:
        SSCFLP += lpSum(x_var[i][j] for j in set_J) == 1

    # Redundant constraints
    for i in set_I:
        for j in set_J:
            SSCFLP += x_var[i][j] <= y_var[j]

    # The problem is solved using PuLP's choice of Solver(the default solver is Coin Cbc)
    SSCFLP.solve(PULP_CBC_CMD(msg=0))

    # The status of the solution is printed to the screen
    print("Status:", LpStatus[SSCFLP.status])

    # The optimal value of the decision variables and the
    # optimised objective function value is printed to the screen
#    for v in SSCFLP.variables():
#        if(v.varValue>0):
#            print(v.name, "=", v.varValue)


    solution = {}
    
    solution["status"] =  LpStatus[SSCFLP.status]

    solution["openfacilities"] = [ i for i in set_J if value(y_var[i]) >= 0.9]

    solution["deliveriesfor_customer"] = [ j for i in set_I for j in set_J if value(x_var[i][j]) >= 0.9]

     print ("Objective value SSCFLP = ", value(SSCFLP.objective))
    solution["objective"] = value(SSCFLP.objective)
    json_object = json.dumps(solution, indent=4)

    with open("Reponse", "w") as outfile:
           outfile.write(json_object)
