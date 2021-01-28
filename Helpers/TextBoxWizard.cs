using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace MyFunds.Helpers
{
    public static class TextBoxWizard
    {
        public static TextBox SetOnlyNumericsFilter(TextBox box)
        {
            box.Loaded += (s, e) =>
            {
                box.Text = "0";
            };
            box.PreviewKeyDown += (s, e) =>
            {
                if (e.Key == Key.Space)
                {
                    e.Handled = true;
                }
                if (e.Key == Key.Back)
                {
                    e.Handled = true;
                    if ((box.Text.Length == box.SelectionLength) || (box.SelectionStart == 1 && box.Text.Length == 1))
                    {
                        box.Text = "0";
                        box.CaretIndex = 0;
                        return;
                    }
                    int selectionStart = box.SelectionStart;
                    if (box.SelectionLength != 0)
                    {
                        box.Text = box.Text.Substring(0, box.SelectionStart) + box.Text.Substring(box.SelectionStart + box.SelectionLength, box.Text.Length - box.SelectionStart - box.SelectionLength);
                    }
                    else
                    {
                        if (selectionStart != 0)
                        {
                            box.Text = box.Text.Remove(selectionStart - 1, 1);
                            selectionStart -= 1;
                        }
                    }
                    while (box.Text[0] == '0' && box.Text.Length != 1)
                    {
                        box.Text = box.Text.Substring(1, box.Text.Length - 1);
                    }
                    box.CaretIndex = selectionStart;
                }
            };
            box.PreviewTextInput += (s, e) =>
            {
                Regex regex = new Regex(@"[0-9]");
                if (regex.IsMatch(e.Text) == false)
                {
                    e.Handled = true;
                    return;
                }
                if (box.Text != string.Empty)
                {
                    if (box.SelectionStart == 0 && e.Text == "0" && box.SelectionLength != box.Text.Length)
                    {
                        e.Handled = true;
                        return;
                    }
                    if (box.Text[0] == '0')
                    {
                        if (box.SelectionStart == 0)
                        {
                            box.Text = string.Empty;
                        }
                        else
                        {
                            e.Handled = true;
                            return;
                        }
                    }
                }
            };
            DataObject.AddPastingHandler(box, (s, e) =>
            {
                e.CancelCommand();
                Regex regex = new Regex(@"^\d+$");
                string text = (string)e.DataObject.GetData(typeof(string));
                if (e.DataObject.GetDataPresent(typeof(string)))
                {
                    if (box.Text != string.Empty)
                    {
                        if (box.Text[0] == '0')
                        {
                            if (box.CaretIndex != 0)
                            {
                                return;
                            }
                            else
                            {
                                foreach (var symbol in text)
                                {
                                    if (!regex.IsMatch(symbol.ToString()))
                                    {
                                        return;
                                    }
                                }
                                box.Text = string.Empty;
                            }
                        }
                    }
                    int selectionStart = box.SelectionStart;
                    if (selectionStart == 0)
                    {
                        while (text[0] == '0' && text.Length != 1)
                        {
                            text = text.Substring(1, text.Length - 1);
                        }
                        if (text == "0")
                        {
                            return;
                        }
                    }
                    foreach (var symbol in text)
                    {
                        if (!regex.IsMatch(symbol.ToString()))
                        {
                            return;
                        }
                    }
                    box.Text = box.Text.Insert(selectionStart, text);
                    box.SelectionStart = selectionStart + text.Length;
                }
            });
            box.LostFocus += (s, e) =>
            {
                if (box.Text == string.Empty)
                {
                    box.Text = "0";
                    return;
                }
                if ((s as TextBox).Text != string.Empty)
                {
                    string text = (s as TextBox).Text;
                    while (text[0] == '0' && text.Length != 1)
                    {
                        text = text.Substring(1, text.Length - 1);
                    }
                    (s as TextBox).Text = text;
                }
            };
            return box;
        }
    }
}
