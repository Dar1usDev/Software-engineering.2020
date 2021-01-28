using MyFunds.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyFunds.ViewModel
{
    public enum IntervalsList
    {
        Year = 0,
        Month, Week, Day,
        Last30days, Last7days
    }

    public class MyFundsInputData
    {
        public List<MoneyManipulation> Manipulations { get; set; }
        public MyFundsInputData(List<MoneyManipulation> manipulations)
        {
            Manipulations = manipulations;
        }
    }

    public class MyFundsOutputData
    {
        public MoneyManipulation NewManipulation { get; set; }
        public MyFundsOutputData(MoneyManipulation newManipulation)
        {
            NewManipulation = newManipulation;
        }
    }
}
