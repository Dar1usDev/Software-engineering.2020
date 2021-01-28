using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyFunds.Helpers
{
    public static class StringFormattingWizard
    {
        public static string ConvertToMoneyFormat(string s)
        {
            int i = s.Length - 3;
            while (i > 0)
            {
                s = s.Insert(i, " ");
                i -= 3;
            }
            return s;
        }
    }
}
