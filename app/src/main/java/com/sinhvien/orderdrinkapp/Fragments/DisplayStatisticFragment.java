package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sinhvien.orderdrinkapp.Activities.DetailStatisticActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayStatistic;
import com.sinhvien.orderdrinkapp.DAO.DonDatDAO;
import com.sinhvien.orderdrinkapp.DTO.DonDatDTO;
import com.sinhvien.orderdrinkapp.R;

import java.util.List;

public class DisplayStatisticFragment extends Fragment {

    ListView lvStatistic;
    List<DonDatDTO> donDatDTOS;
    DonDatDAO donDatDAO;
    AdapterDisplayStatistic adapterDisplayStatistic;
    FragmentManager fragmentManager;
    int madon, manv, maban;
    String ngaydat, tongtien;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaystatistic_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý thống kê");
        setHasOptionsMenu(true);

        lvStatistic = (ListView)view.findViewById(R.id.lvStatistic);
        donDatDAO = new DonDatDAO(getActivity());

        donDatDTOS = donDatDAO.LayDSDonDat();
        adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(),R.layout.custom_layout_displaystatistic,donDatDTOS);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();

        lvStatistic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                madon = donDatDTOS.get(position).getMaDonDat();
                manv = donDatDTOS.get(position).getMaNV();
                maban = donDatDTOS.get(position).getMaBan();
                ngaydat = donDatDTOS.get(position).getNgayDat();
                tongtien = donDatDTOS.get(position).getTongTien();

                Intent intent = new Intent(getActivity(), DetailStatisticActivity.class);
                intent.putExtra("madon",madon);
                intent.putExtra("manv",manv);
                intent.putExtra("maban",maban);
                intent.putExtra("ngaydat",ngaydat);
                intent.putExtra("tongtien",tongtien);
                startActivity(intent);
            }
        });
        registerForContextMenu(lvStatistic);
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;

        switch (id) {
            case R.id.itDelete:
                // Xóa đơn đặt tại vị trí được chọn
                XoaDonDat(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void XoaDonDat(int position) {
        // Lấy thông tin đơn đặt tại vị trí được chọn
        int maDonDat = donDatDTOS.get(position).getMaDonDat();

        // Thực hiện xóa đơn đặt
        boolean ktra = donDatDAO.XoaDonDat(maDonDat);
        if (ktra) {
            // Nếu xóa thành công, cập nhật lại danh sách và thông báo
            donDatDTOS.remove(position);
            adapterDisplayStatistic.notifyDataSetChanged();
            Toast.makeText(getActivity(), getResources().getString(R.string.delete_sucessful), Toast.LENGTH_SHORT).show();
            HienThiDSDonDat();
        } else {
            // Nếu xóa thất bại, hiển thị thông báo lỗi
            Toast.makeText(getActivity(), getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        }
    }
    private void HienThiDSDonDat() {
        donDatDTOS = donDatDAO.LayDSDonDat();
        adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(), R.layout.custom_layout_displaystatistic, donDatDTOS);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();
    }
}
