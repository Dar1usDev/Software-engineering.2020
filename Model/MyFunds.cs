using MyFunds.Helpers;
using MyFunds.ViewModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace MyFunds.Model
{
    [Serializable]
    public class MyFunds : MyFundsBase
    {
        private List<MoneyManipulation> vault;
        [NonSerialized]
        private BinaryFormatter formatter = new BinaryFormatter();

        public MyFunds()
        {
            Load();
        }

        public override MyFundsInputData GetDataFromInterval(IntervalsList interval, DateTime startingPoint)
        {
            DateTime endingPoint = new DateTime();
            switch (interval)
            {
                case IntervalsList.Year: endingPoint = DateTimeRoundingWizard.FloorByYear(startingPoint); break;
                case IntervalsList.Month: endingPoint = DateTimeRoundingWizard.FloorByMonth(startingPoint); break;
                case IntervalsList.Week: endingPoint = DateTimeRoundingWizard.FloorByWeek(startingPoint); break;
                case IntervalsList.Day: endingPoint = DateTimeRoundingWizard.FloorByDay(startingPoint); break;
                case IntervalsList.Last30days: endingPoint = startingPoint.AddDays(-30).AddDays(-1).AddSeconds(1); break;
                case IntervalsList.Last7days: endingPoint = startingPoint.AddDays(-7).AddDays(-1).AddSeconds(1); break;
            }
            var findedManipulations = from m in vault
                                      where m.Date >= endingPoint && m.Date <= startingPoint
                                      orderby m.Date
                                      select m;
            return new MyFundsInputData(findedManipulations.ToList());
        }

        public override bool RegisterManipulation(MyFundsOutputData data)
        {
            vault.Add(data.NewManipulation);
            Save();
            return true;
        }

        public override bool DropManipulation(MoneyManipulation manipulation)
        {
            vault.Remove(manipulation);
            Save();
            return true;
        }

        protected virtual void Save()
        {
            try
            {
                using (FileStream fs = new FileStream("MyFunds.dat", FileMode.OpenOrCreate))
                {
                    formatter.Serialize(fs, vault);
                    Debug.WriteLine("Данные сохранены.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message); 
            }
        }

        protected virtual void Load()
        {
            try
            {
                using (FileStream fs = new FileStream("MyFunds.dat", FileMode.OpenOrCreate))
                {
                    var deserilizedVault = (List<MoneyManipulation>)formatter.Deserialize(fs);
                    vault = deserilizedVault;
                    Debug.WriteLine("Данные загружены.");
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex.Message);
                vault = new List<MoneyManipulation>();
                Debug.WriteLine("Данные созданы.");
            }
        }
    }
}
