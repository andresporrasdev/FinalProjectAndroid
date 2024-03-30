package algonquin.cst2335.finalprojectandroid.dictionary;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for managing the data related to the dictionary feature in the application.
 */
public class DictionaryViewModel extends ViewModel {
    /**
     * MutableLiveData containing a list of dictionary items.
     * This LiveData is observable, allowing UI components to react to changes in the list of words.
     */
    public MutableLiveData<ArrayList<DictionaryItem>> words = new MutableLiveData<>();

    /**
     * MutableLiveData containing the currently selected dictionary item.
     * This LiveData is observable, allowing UI components to react to changes in the selected word.
     */
    public MutableLiveData<DictionaryItem> selectedWord = new MutableLiveData<>();
}