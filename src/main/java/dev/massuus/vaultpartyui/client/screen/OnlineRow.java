package dev.massuus.vaultpartyui.client.screen;

final class OnlineRow {
    final OnlinePlayer player;
    final RowState state;
    final boolean favorite;

    OnlineRow(OnlinePlayer player, RowState state, boolean favorite) {
        this.player = player;
        this.state = state;
        this.favorite = favorite;
    }
}
