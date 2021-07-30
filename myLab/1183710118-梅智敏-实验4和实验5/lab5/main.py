import sys

from PyQt5.QtWidgets import QApplication

from gui import Ui_MainWindow
from query_optimize import MainWindow

app = QApplication(sys.argv)
main_win = MainWindow(Ui_MainWindow())
main_win.show()
sys.exit(app.exec_())
