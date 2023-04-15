package org.shareinvetory.shareinventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.Inventory;

public final class ShareInventory extends JavaPlugin implements Listener {

    private Inventory sharedInventory;

    @Override
    public void onEnable() {
        // 서버 시작 시 실행되는 코드
        // 플러그인에서 사용할 인벤토리 생성
        sharedInventory = Bukkit.createInventory(null, 54, "Shared Inventory");

        // 모든 플레이어들의 인벤토리를 플러그인에서 생성한 인벤토리로 설정
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().setContents(sharedInventory.getContents());
        }

        // 이벤트 핸들러 등록
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // 서버 종료 시 실행되는 코드
        // 모든 플레이어들의 인벤토리를 기본 인벤토리로 변경
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().setContents(player.getInventory().getContents());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 인벤토리 클릭 이벤트가 발생했을 때 실행되는 코드
        // 클릭한 인벤토리가 플러그인에서 생성한 인벤토리인 경우
        if (event.getClickedInventory() == sharedInventory) {
            // 클릭한 아이템이 null이 아니면서 빈 슬롯이 아닌 경우
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                // 모든 플레이어들의 인벤토리에서 해당 아이템 제거
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.getInventory().removeItem(event.getCurrentItem());
                }

                // 플러그인에서 생성한 인벤토리에 해당 아이템 추가
                sharedInventory.addItem(event.getCurrentItem());
            }
        }
    }
}
