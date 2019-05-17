package com.onaple.epicboundaries;

import com.onaple.crowdbinding.service.GroupService;
import cz.neumimto.core.ioc.IoC;
import cz.neumimto.rpg.players.CharacterService;
import cz.neumimto.rpg.players.IActiveCharacter;
import cz.neumimto.rpg.players.parties.Party;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @autor Hugo on 17/05/19.
 */
@Singleton
public class GroupHandler {
    public GroupHandler() {
    }
    private List<Player> getGroupMember(Player player) {
        CharacterService characterService = IoC.get().build(CharacterService.class);
        IActiveCharacter activeCharacter = characterService.getCharacter(player.getUniqueId());
        Party party = activeCharacter.getParty();
        return party.getPlayers().stream().map(iActiveCharacter -> iActiveCharacter.getPlayer()).collect(Collectors.toList());
    }
    public List<Player> getGroup(Player player) throws CommandException {
        List<Player> players = new ArrayList<>();
        Optional<GroupService> optionalGroupService;
        if (Sponge.getPluginManager().isLoaded("crowdbinding")) {
            try {
                optionalGroupService = Sponge.getServiceManager().provide(GroupService.class);
                if (optionalGroupService.isPresent()) {
                    GroupService groupService = optionalGroupService.get();
                    groupService.getGroupId(player).ifPresent(gid -> players.addAll(groupService.getMembers(gid)));

                } else {
                    EpicBoundaries.getLogger().warn("Could not get CrowdBinding GroupService class");
                }
            } catch (NoClassDefFoundError e) {
                EpicBoundaries.getLogger().warn("Could not get CrowdBinding GroupService class");
            }
        } else if (Sponge.getPluginManager().isLoaded("nt-rpg")) {
            try {
                EpicBoundaries.getLogger().debug("nt-rpg loaded, check for group members [{}] ",
                        getGroupMember(player));
                players.addAll(getGroupMember(player));
            } catch (NoClassDefFoundError e) {
                EpicBoundaries.getLogger().warn("Could not get NT-RPG CharacterService class");

            }
        } else {
            throw new CommandException(Text.of("You need either ")
                    .concat(Text.builder("CrowdBinding or NT-RPG").style(TextStyles.BOLD).build())
                    .concat(Text.of(" use group teleportation")));
        }
        return players;
    }
}
