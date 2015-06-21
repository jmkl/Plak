package hello.dcsms.plak.task.iface;

import java.util.List;

import hello.dcsms.plak.manual.AutoCompleteData;

public interface OnLoadAutoComplete {

	void onComplete(List<AutoCompleteData> completedata);
}
