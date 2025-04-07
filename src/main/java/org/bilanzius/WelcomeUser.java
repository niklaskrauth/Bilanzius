package org.bilanzius;

import org.bilanzius.utils.Localization;

public class WelcomeUser
{
    static Localization localization = Localization.getInstance();

    public static void welcomeMessage()
    {
        String ASCII_BILANZIUS = """
                        ####################################################################################################################################
                                                                                                                                                           \s
                        BBBBBBBBBBBBBBBBB     iiii  lllllll                                                        iiii                                    \s
                        B::::::::::::::::B   i::::i l:::::l                                                       i::::i                                   \s
                        B::::::BBBBBB:::::B   iiii  l:::::l                                                        iiii                                    \s
                        BB:::::B     B:::::B        l:::::l                                                                                                \s
                          B::::B     B:::::Biiiiiii  l::::l   aaaaaaaaaaaaa   nnnn  nnnnnnnn    zzzzzzzzzzzzzzzzziiiiiii uuuuuu    uuuuuu      ssssssssss  \s
                          B::::B     B:::::Bi:::::i  l::::l   a::::::::::::a  n:::nn::::::::nn  z:::::::::::::::zi:::::i u::::u    u::::u    ss::::::::::s \s
                          B::::BBBBBB:::::B  i::::i  l::::l   aaaaaaaaa:::::a n::::::::::::::nn z::::::::::::::z  i::::i u::::u    u::::u  ss:::::::::::::s\s
                          B:::::::::::::BB   i::::i  l::::l            a::::a nn:::::::::::::::nzzzzzzzz::::::z   i::::i u::::u    u::::u  s::::::ssss:::::s
                          B::::BBBBBB:::::B  i::::i  l::::l     aaaaaaa:::::a   n:::::nnnn:::::n      z::::::z    i::::i u::::u    u::::u   s:::::s  ssssss\s
                          B::::B     B:::::B i::::i  l::::l   aa::::::::::::a   n::::n    n::::n     z::::::z     i::::i u::::u    u::::u     s::::::s     \s
                          B::::B     B:::::B i::::i  l::::l  a::::aaaa::::::a   n::::n    n::::n    z::::::z      i::::i u::::u    u::::u        s::::::s  \s
                          B::::B     B:::::B i::::i  l::::l a::::a    a:::::a   n::::n    n::::n   z::::::z       i::::i u:::::uuuu:::::u  ssssss   s:::::s\s
                        BB:::::BBBBBB::::::Bi::::::il::::::la::::a    a:::::a   n::::n    n::::n  z::::::zzzzzzzzi::::::iu:::::::::::::::uus:::::ssss::::::s
                        B:::::::::::::::::B i::::::il::::::la:::::aaaa::::::a   n::::n    n::::n z::::::::::::::zi::::::i u:::::::::::::::us::::::::::::::s\s
                        B::::::::::::::::B  i::::::il::::::l a::::::::::aa:::a  n::::n    n::::nz:::::::::::::::zi::::::i  uu::::::::uu:::u s:::::::::::ss \s
                        BBBBBBBBBBBBBBBBB   iiiiiiiillllllll  aaaaaaaaaa  aaaa  nnnnnn    nnnnnnzzzzzzzzzzzzzzzzziiiiiiii    uuuuuuuu  uuuu  sssssssssss   \s
                                                                                                                                                           \s
                        ####################################################################################################################################
                        """;

        System.out.println(localization.getMessage("greeting"));
        System.out.println(ASCII_BILANZIUS);
    }
}
