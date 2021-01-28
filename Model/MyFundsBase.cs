using MyFunds.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyFunds.Model
{
    abstract public class MyFundsBase
    {
        abstract public bool RegisterManipulation(MyFundsOutputData data);
        abstract public MyFundsInputData GetDataFromInterval(IntervalsList interval, DateTime startingPoint);
        abstract public bool DropManipulation(MoneyManipulation manipulation);
    }
}
