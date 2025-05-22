package com.example.proyekakhirpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;
    private static final int DETAIL_REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycle_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        postList = new ArrayList<>();
        postList.add(new Post("Pandu Satria", "Kadang hal kecil kayak ngasih satu porsi makan bisa bikin orang lain tersenyum lebar. Dan itu rasanya luar biasa banget. Hari ini aku bersyukur banget karena masih dikasih kesempatan buat bantu sesama, walau cuma lewat makanan.\n" +
                "\n" +
                "Lihat ekspresi bahagia mereka saat nerima makanan tuh bikin hati hangat. Rasanya kayak “wah, ternyata hal sederhana ini bisa berarti banget ya buat orang lain.”\n" +
                "\n" +
                "Makanya, aku pengen ajak kamu juga buat ikut berbagi. Nggak harus banyak, yang penting tulus. Bareng-bareng, kita bisa bantu lebih banyak orang dan bikin dunia ini jadi tempat yang lebih baik —satu porsi makanan, satu senyuman.", R.drawable.pc_makanan, 10, 5, 7));
        postList.add(new Post("Akhtar Hafiz", "Terima kasih kepada para donatur yang telah ikut berbagi. Senang sekali rasanya bisa berbagi kepada mereka yang membutuhkan. Setiap rupiah yang kalian donasikan akan sangat disyukuri oleh mereka.", R.drawable.pc_makanan, 8, 12, 3));
        postList.add(new Post("Rizky Pratama",
                "Alhamdulillah, hari ini berhasil menyalurkan 15 paket sembako ke warga yang terdampak PHK di sekitar lingkungan. Terima kasih untuk teman-teman LeftoverShare yang udah donasi. Semoga jadi berkah untuk kita semua!",
                R.drawable.pc_makanan, 14, 8, 2));
        postList.add(new Post("Kinky Maylana",
                "Pagi tadi, kami berhasil membagikan nasi bungkus sisa event ke para pengemudi ojek online. Mereka nggak nyangka bisa dapat sarapan gratis. Kecil bagi kita, tapi besar buat mereka. Yuk terus berbagi lewat LeftoverShare!",
                R.drawable.pc_makanan, 20, 15, 6));

        postList.add(new Post("Neyla",
                "Kami baru aja mulai galang dana untuk membantu ibu-ibu janda lansia di sekitar pasar tradisional. Targetnya Rp1 juta untuk 20 paket sembako. Yuk bantu mereka lewat fitur galang dana di LeftoverShare!",
                R.drawable.pc_makanan, 5, 3, 1));

        postList.add(new Post("Nadya Ramadhani",
                "Beli makanan sisa restoran favoritku dari LeftoverShare, cuma Rp10.000-an. Makanannya masih fresh dan enak banget. Jadi bisa bantu kurangi food waste sekaligus hemat. Recommended banget!",
                R.drawable.pc_makanan, 18, 11, 4));

        postList.add(new Post("Fahri Maulana",
                "Hari ini dapet kesempatan jadi relawan buat bagiin makanan dari LeftoverShare ke shelter anak jalanan. Seru dan bikin haru. Yuk ikut jadi volunteer juga!",
                R.drawable.pc_makanan, 13, 7, 2));

        adapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((post, position) -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("nama", post.getNama());
            intent.putExtra("deskripsi", post.getDeskripsi());
            intent.putExtra("image", post.getImageRes());
            intent.putExtra("smile", post.getReactSmile());
            intent.putExtra("haru", post.getReactHaru());
            intent.putExtra("hug", post.getReactHug());
            startActivity(intent);
        });

        return view;
    }
}
