package ch.heigvd.iict.daa.template

import DataRepository
import NotesAdapter
import NotesViewModel
import NotesViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentNotes : Fragment() {

    private lateinit var adapter: NotesAdapter
    private val viewModel: NotesViewModel by viewModels {
        val application = requireActivity().application
        val database = (application as App).database
        val repository = DataRepository(database.noteDao())
        NotesViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notes_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        adapter = NotesAdapter(emptyList())
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Observe the LiveData from the ViewModel
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            adapter.noteItems = notes.map { NoteItem.SimpleNote(it.note) }
        }
    }
}
