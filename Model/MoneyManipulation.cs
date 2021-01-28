using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyFunds.Model
{
    [Serializable]
    public class MoneyManipulation
    {
        public int Amount { get; private set; }
        public DateTime Date { get; private set; }
        public string Type { get; private set; }
        public string Comment { get; private set; }

        public MoneyManipulation(int amount, DateTime date, string type = "Другое", string comment = null)
        {
            Amount = amount;
            Date = date;
            Type = type;
            Comment = comment;
        }
    }
}
