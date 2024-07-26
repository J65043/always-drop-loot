package me.sargunvohra.mcmods.alwaysdroploot.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTestHelper;

public class TestUtil {

  public static void runCommand(GameTestHelper helper, String command) {
    var server = helper.getLevel().getServer();

    CommandSourceStack commandsourcestack  = server.createCommandSourceStack();
    CommandDispatcher<CommandSourceStack> commanddispatcher = server.getCommands().getDispatcher();
    ParseResults<CommandSourceStack> results = commanddispatcher.parse(command,commandsourcestack);


    server.getCommands().performCommand(results,command);



  }
}
