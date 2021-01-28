using MyFunds.Helpers;
using MyFunds.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.Security.RightsManagement;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace MyFunds.ViewModel
{
    public class MyFunds : ViewModel
    {
        private Model.MyFundsBase model;
        public MyFunds(Model.MyFundsBase modelImplementation)
        {
            model = modelImplementation;
            SelectedInterval = IntervalsCollection[3];
            SelectedManipulationType = "Другое";
            SelectedDate = DateTime.Today;
            NegativeManipulation = true;
        }

        //Отображение даты--------------------------------------------------//
        #region Отображение даты
        private string formalDate;
        public string FormalDate
        {
            get { return formalDate; }
            set { formalDate = value; NotifyThatPropertyChanged("FormalDate"); }
        }
        private string presentationDate;
        public string PresentationDate
        {
            get { return presentationDate; }
            set { presentationDate = value; NotifyThatPropertyChanged("PresentationDate"); }
        }

        #endregion
        //Главная-----------------------------------------------------------//
        #region Главная
        private int totalAmount;
        private int income;
        private int expenses;
        public string TotalAmount
        {
            get { return StringFormattingWizard.ConvertToMoneyFormat(totalAmount.ToString()); }
            set
            {
                int parsedValue = int.Parse(value);
                if (parsedValue < 0)
                {
                    NegativeTotalAmount = true;
                }
                else
                {
                    NegativeTotalAmount = false;
                }
                totalAmount = parsedValue; NotifyThatPropertyChanged("TotalAmount"); 
            }
        }
        public string Income
        {
            get { return StringFormattingWizard.ConvertToMoneyFormat(income.ToString()); ; }
            set { income = int.Parse(value); NotifyThatPropertyChanged("Income"); }
        }
        public string Expenses
        {
            get { return StringFormattingWizard.ConvertToMoneyFormat(expenses.ToString()); ; }
            set { expenses = int.Parse(value); NotifyThatPropertyChanged("Expenses"); }
        }
        public List<string> IntervalsCollection
        {
            get
            {
                return new List<string>
                {
                    "год",
                    "месяц",
                    "неделю",
                    "день",
                    "30 дней",
                    "7 дней",
                };
            }
        }
        private string declinationForInterval;
        public string DeclinationForInterval
        {
            get { return declinationForInterval; }
            set { declinationForInterval = value; NotifyThatPropertyChanged("DeclinationForInterval"); }
        }
        private string selectedInterval;
        public string SelectedInterval
        {
            get { return selectedInterval; }
            set { selectedInterval = value; SetData(); NotifyThatPropertyChanged("SelectedInterval"); }
        }
        private bool negativeTotalAmount = false;
        public bool NegativeTotalAmount
        {
            get { return negativeTotalAmount; }
            set { negativeTotalAmount = value; NotifyThatPropertyChanged("NegativeTotalAmount"); }
        }

        private void SetData()
        {
            MyFundsInputData data = null;
            switch (SelectedInterval)
            {
                case "год": DeclinationForInterval = "последний"; data = model.GetDataFromInterval(IntervalsList.Year, SelectedDate); break;
                case "месяц": DeclinationForInterval = "последний"; data = model.GetDataFromInterval(IntervalsList.Month, SelectedDate); break;
                case "неделю": DeclinationForInterval = "последнюю"; data = model.GetDataFromInterval(IntervalsList.Week, SelectedDate); break;
                case "день": DeclinationForInterval = "последний"; data = model.GetDataFromInterval(IntervalsList.Day, SelectedDate); break;
                case "30 дней": DeclinationForInterval = "последние"; data = model.GetDataFromInterval(IntervalsList.Last30days, SelectedDate); break;
                case "7 дней": DeclinationForInterval = "последние"; data = model.GetDataFromInterval(IntervalsList.Last7days, SelectedDate); break;
            }
            Manipulations = new ObservableCollection<MoneyManipulation>(data.Manipulations);
            int dataExpenses = 0;
            int dataIncome = 0;
            foreach (MoneyManipulation manipulation in Manipulations)
            {
                if (manipulation.Amount < 0)
                {
                    dataExpenses -= manipulation.Amount;
                }
                else
                {
                    dataIncome += manipulation.Amount;
                }
            }
            Expenses = dataExpenses.ToString();
            Income = dataIncome.ToString();
            TotalAmount = (dataIncome - dataExpenses).ToString();
        }

        #endregion
        //Учет доходов/расходов---------------------------------------------//
        #region Учет доходов/расходов
        private bool negativeManipulation = true;
        public bool NegativeManipulation
        {
            get { return negativeManipulation; }
            set
            {
                negativeManipulation = value;
                if (value == true)
                {
                    ManipulationTypesCollection = expensesManipulationTypesCollection;
                }
                else
                {
                    ManipulationTypesCollection = incomeManipulationTypesCollection;
                }
                NotifyThatPropertyChanged("NegativeManipulation");
            }
        }
        private List<string> manipulationTypesCollection;
        public List<string> ManipulationTypesCollection
        {
            get { return manipulationTypesCollection; }
            set { manipulationTypesCollection = value; NotifyThatPropertyChanged("ManipulationTypesCollection"); }
        }
        private List<string> incomeManipulationTypesCollection = new List<string>
        {
            "Зарплата",
            "Другое"
        };
        private List<string> expensesManipulationTypesCollection = new List<string>
        {
            "Супермаркет",
            "Еда вне дома",
            "Одежда",
            "Интернет",
            "Подарки",
            "Развлечения",
            "Такси",
            "Общественный траснпорт",
            "Медицина",
            "Другое"
        };
        private string selectedManipulationType;
        public string SelectedManipulationType
        {
            get { return selectedManipulationType; }
            set { selectedManipulationType = value; NotifyThatPropertyChanged("SelectedManipulationType"); }
        }
        private int manipulationAmount;
        public string ManipulationAmount
        {
            get { return manipulationAmount.ToString(); }
            set { manipulationAmount = int.Parse(value); NotifyThatPropertyChanged("ManipulationAmount"); }
        }
        private string comment = "";
        public string Comment { get { return comment; } set { comment = value; NotifyThatPropertyChanged("Comment"); } }
        private BindingMethod switchManipulatingType;
        public BindingMethod SwitchManipulatingType
        {
            get
            {
                return switchManipulatingType ?? (switchManipulatingType = new BindingMethod(property =>
                {
                    NegativeManipulation = !NegativeManipulation;
                }));
            }
        }
        private BindingMethod registerManipulation;
        public BindingMethod RegisterManipulation
        {
            get
            {
                return registerManipulation ?? (registerManipulation = new BindingMethod(property =>
                {
                    MoneyManipulation manipulation;
                    if (negativeManipulation == true)
                    {
                        if (manipulationAmount == 0)
                        {
                            if (MessageBox.Show("Вы указали сумму расхода равной 0.\nПродолжить?", "Предупреждение", MessageBoxButton.YesNo, MessageBoxImage.Question) == MessageBoxResult.No)
                            {
                                return;
                            }
                        }
                        manipulation = new MoneyManipulation(-manipulationAmount, selectedDate, selectedManipulationType, Comment != string.Empty ? Comment : "Комментарий не указан");
                    }
                    else
                    {
                        if (manipulationAmount == 0)
                        {
                            if (MessageBox.Show("Вы указали сумму дохода равной 0.\nПродолжить?", "Предупреждение", MessageBoxButton.YesNo, MessageBoxImage.Question) == MessageBoxResult.No)
                            {
                                return;
                            }
                        }
                        manipulation = new MoneyManipulation(manipulationAmount, selectedDate, selectedManipulationType, Comment != string.Empty ? Comment : "Комментарий не указан");
                    }
                    var data = new MyFundsOutputData(manipulation);
                    model.RegisterManipulation(data);
                    Manipulations.Add(manipulation);
                    if (negativeManipulation == true)
                    {
                        Expenses = (expenses += manipulationAmount).ToString();
                        TotalAmount = (totalAmount - manipulationAmount).ToString();
                    }
                    else
                    {
                        Income = (income += manipulationAmount).ToString();
                        TotalAmount = (totalAmount + manipulationAmount).ToString();
                    }
                    ManipulationAmount = "0";
                    SelectedManipulationType = "Другое";
                    Comment = string.Empty;
                }));
            }
        }

        #endregion
        //История-----------------------------------------------------------//
        #region История
        private ObservableCollection<MoneyManipulation> manipulations;
        public ObservableCollection<MoneyManipulation> Manipulations
        {
            get { return manipulations; }
            set { manipulations = value; NotifyThatPropertyChanged("Manipulations"); }
        }
        public MoneyManipulation SelectedRow { get; set; }
        private BindingMethod dropManipulation;
        public BindingMethod DropManipulation
        {
            get
            {
                return dropManipulation ?? (dropManipulation = new BindingMethod(property =>
                {
                    model.DropManipulation(SelectedRow);
                    if (SelectedRow.Amount < 0)
                    {
                        Expenses = (expenses += SelectedRow.Amount).ToString();
                        TotalAmount = (totalAmount - SelectedRow.Amount).ToString();
                    }
                    else
                    {
                        Income = (income -= SelectedRow.Amount).ToString();
                        TotalAmount = (totalAmount - SelectedRow.Amount).ToString();
                    }
                    Manipulations.Remove(SelectedRow);
                }));
            }
        }

        #endregion
        //Элементы управления-----------------------------------------------//
        #region Элементы управления
        public enum MenuList { Main = 0, Adding = 1, History = 2 }
        private MenuList selectedMenu = MenuList.Main;
        public MenuList SelectedMenu
        {
            get { return selectedMenu; }
            set { selectedMenu = value; NotifyThatPropertyChanged("SelectedMenu"); }
        }
        private BindingMethod switchMenu;
        public BindingMethod SwitchMenu
        {
            get
            {
                return switchMenu ?? (switchMenu = new BindingMethod(property =>
                {
                    switch ((string)property)
                    {
                        case "Main": SelectedMenu = MenuList.Main; break;
                        case "Adding": SelectedMenu = MenuList.Adding; break;
                        case "History": SelectedMenu = MenuList.History; break;
                    }
                }));
            }
        }

        #endregion
        //Календарь---------------------------------------------------------//
        #region Календарь
        private bool calendarVisibility = false;
        public bool CalendarVisibility
        {
            get { return calendarVisibility; }
            set { calendarVisibility = value; NotifyThatPropertyChanged("CalendarVisibility"); }
        }
        private DateTime selectedDate;
        public DateTime SelectedDate
        {
            get { return selectedDate; }
            set
            {
                CalendarVisibility = false;
                selectedDate = value.AddDays(1).AddSeconds(-1);
                SetData(); NotifyThatPropertyChanged("SelectedDate");
                PresentationDate = $"{(value.Day.ToString().Length == 1 ? $"0{value.Day}" : $"{value.Day}")}." +
                    $"{(value.Month.ToString().Length == 1 ? $"0{value.Month}" : $"{value.Month}")}";
                if (DateTime.Now.Date == value.Date)
                {
                    FormalDate = "Сегодня";
                }
                else if (DateTime.Now.Date > value.Date)
                {
                    FormalDate = "Ранее";
                }
                else
                {
                    FormalDate = "Скоро";
                }
            }
        }
        private BindingMethod changeCalendarState;
        public BindingMethod ChangeCalendarState
        {
            get
            {
                return changeCalendarState ?? (changeCalendarState = new BindingMethod(property =>
                {
                    switch ((string)property)
                    {
                        case "Open": CalendarVisibility = true; break;
                        case "Close": CalendarVisibility = false; break;
                    }
                }));
            }
        }

        #endregion
    }
}
