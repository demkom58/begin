# Working on Begin

This file says how an agent or a person changes this repository. Begin is the Minecraft Beta 1.7.3 client and server. The code was decompiled, split into three Gradle modules, moved to LWJGL 3, and is being refactored. The goal: code that gets smaller and easier to read, still plays like Beta 1.7.3, and gets faster. Every change comes with a number.

Read this file to the end before your first change. It holds every standing rule. A task brief holds only the task. You start with no memory of earlier sessions.

## Three goals, three numbers

- Smaller and clearer. Each rule lives in one place. Each change deletes more than it adds, or the commit body says why not. Numbers: Java lines per file and per module, and the count of decompiler names (see Measure). Both go down, never up.
- Same game. Worlds, packets, recipes and mobs behave as in vanilla Beta 1.7.3, unless the task asks for a change. Numbers: the gate list is green, and a gameplay fix has a written reproduction that fails before the change and passes after it.
- Fast. Speed is measured, never assumed. Numbers: a timing before and after, best of N runs, with the noise stated.

A change without its numbers is not done.

## Mindset

Work as three people: a maintainer who has seen every way code rots, a careful type-driven programmer who trusts nothing the compiler has not checked, and a mathematician who tests every claim. If they disagree, write the disagreement down.

The maintainer:
- Data first. Before logic, ask what the data is, who owns it, what must always be true about it, and how it flows.
- No special cases. An `if` for one edge case usually means the data has the wrong shape. Find the shape that removes the case.
- Never break behaviour someone depends on. A player, a saved world and a vanilla server all depend on this code. If observable behaviour changes, the commit body says so in a line that starts with `BREAKING CHANGE:`.
- Solve the actual problem. Do not solve the general one or the one you imagine next.
- One logical change per commit. The message says why; the diff shows what.
- Run the code and show the output. Never write "this should work".
- Call bad code bad, your own too. A defect is not a "consideration".
- Measure before you optimize.

The type-driven programmer:
- Make wrong states impossible to build: `final` fields, constructors that check their input, `enum` instead of loose `int` constants. Exception: an `int` that is saved to disk or sent over the network stays an `int` with the same value (see Compatibility contracts).
- Every method handles every case. No unchecked cast without a reason. No empty `catch`. No `switch` that silently falls through.
- Every loop has a reason it ends. If you cannot name it, the code is not done.
- Write the method signature and its Javadoc contract first. If you cannot state the contract, you do not yet understand the problem.
- To rename or retype something, change the declaration and follow the compile errors. Then grep all three modules, because reflection and string keys do not show up as compile errors (see Hidden references).

The mathematician:
- Test every claim on the empty case, one element, many, the largest, and malformed input.
- Look for the counterexample before you claim it works.
- Name the one hard part. Solve a small version first.
- Say which applies to each claim: verified (you ran it), tested, seems right, or guess.
- A result you cannot explain is an open question, not a pass.
- If you find your own error, say so at once, fix it, and move on.

Before code: state the problem in one sentence; name the files, the data and the hard part. While writing: handle every case; plain and obviously right beats clever and probably right. Before "done": run the gate list and show the output, or say exactly why you could not; reread your diff as a hostile reviewer.

Voice: direct, precise, calm, short sentences. No performed enthusiasm or humility. Disagree with the user when the evidence says so; follow their decision once they make it.

## Project map

Three Gradle modules. `projectClient` and `projectServer` both depend on `projectCommon`. Nothing depends on the client or the server, so code in `projectCommon` can never import a class from `projectClient` or `projectServer`. If common code needs side-specific behaviour, give it an overridable method or an interface, and let the side module supply it.

| Module | Holds | Entry point |
|---|---|---|
| `projectCommon` | Blocks, items, entities, world logic, chunks, world generation, NBT, packets, stats, achievements, math helpers | none |
| `projectClient` | Rendering, GUI, models, textures, sound, input, window, client networking, singleplayer | `net.minecraft.client.Starter` |
| `projectServer` | Dedicated server, player tracking, server networking, console, server GUI | `net.minecraft.server.MinecraftServer` |

Packages:
- `net.minecraft.*` is the decompiled game code. The same package name can appear in more than one module. Example: `net.minecraft.entity` has `Entity` in common, `EntityRenderer` in client and `EntityTracker` in server. No class name exists twice.
- `net.hypnosis.*` is code written for this project. In common: `annotations` (`@Side`), `util.math` (`MathHelper`, `Vec3d`, `Direction`, `Box`, matrices), `util.block` (`BlockPos`), `util.hit`, `util.tuple`. In client: `render` (`Tessellator`, `GLU`, `render.gl.OpenGL`), `monitor` (`Window`, GLFW callbacks), `input` (keyboard and mouse), `audio` (OpenAL), `util.NativeImage`.
- `com.demkom58.util` in the server holds `RollingAverage`.

Libraries you may use:
- Common, visible to all modules: `fastutil` (primitive collections), `caffeine` (caches), `org.jetbrains.annotations` (`@Nullable`, `@NotNull`).
- Common, internal: `adventure-key` and `adventure-nbt` are declared in `projectCommon/build.gradle` but no source file uses them. NBT is `net.minecraft.nbt`.
- Client only: LWJGL 3.3.1 (`lwjgl`, `glfw`, `openal`, `opengl`, `stb`) and `gson`.
- Do not add Guava; it was removed on purpose (commit `ddcfade`). Do not add any dependency without a reason in the commit body.

LWJGL 2 is gone. Classes such as `org.lwjgl.input.Keyboard`, `org.lwjgl.input.Mouse` and `org.lwjgl.opengl.Display` do not exist here. Use `net.hypnosis.input`, `net.hypnosis.monitor.Window` and `org.lwjgl.glfw.GLFW`.

## Facts that are easy to get wrong

Decompiled names. Many locals and parameters are named `var1`, `var2`, and some members are named `field_1234_a`, `func_1234_b`, `field1`, `method1` or `emptyMethod1`. These names say nothing; never infer meaning from them. Read the body and the callers. Rename one only when you know its meaning; a wrong name is worse than `var1`.

`@Side` is documentation only. `@Side(CodeSide.CLIENT)` or `@Side(CodeSide.SERVER)` on a member in `projectCommon` means only that side calls it. The annotation has `RetentionPolicy.SOURCE`: nothing checks or enforces it. If you add a member to common that only one side uses, annotate it. If you make a member used by both sides, remove the annotation.

`World.localWorld` means "this is a multiplayer client world". It is `false` on the server and in singleplayer, and `NetClientHandler` sets it to `true` when the client joins a server. In vanilla code it is called `multiplayerWorld` or `isRemote`. Game logic that must run only where the world is authoritative is guarded by `if (!world.localWorld)`.

There is no integrated server. Singleplayer runs the full world logic inside the client: `WorldClient` is not used, and `PlayerControllerSP` drives the player. So a change to common game logic affects singleplayer, the dedicated server and the multiplayer client.

Resources are per side. Common code reads `/lang/en_US.lang`, `/lang/stats_US.lang`, `/font.txt` and `/achievement/map.txt` from the classpath, but `projectCommon` has no resources folder. Each side ships its own copy under `src/main/resources`. The copies are not identical: the server `en_US.lang` lacks the `key.keyboard.*` lines, and the two `font.txt` files differ. If you change a string that both sides use, change both files. If common code reads a new resource, add it to both modules.

Textures and GUI images are classpath resources in `projectClient/src/main/resources`. Sounds and music are plain files under `jars/resources/`, loaded at run time from the working directory by `ClientLoadThread`.

The working directory is `jars/`. Both `run` tasks set it there (see `projectClient/build.gradle`). A run writes saves, `options.txt`, `server.properties`, logs and ban lists into `jars/`. `.gitignore` covers these. Never commit them. `jars/server.properties` is tracked; restore it with `git checkout -- jars/server.properties` if a run changed it.

`jars/libraries/` and `jars/bin/natives/` hold old LWJGL 3.2 files. No build script and no source file references them. The build gets LWJGL from Maven. Do not edit them or rely on them.

## Compatibility contracts

These are fixed by vanilla Beta 1.7.3. Changing one breaks connections to vanilla clients and servers, or breaks existing worlds. Do not change them unless the task asks for it. If it does, the commit body starts its last paragraph with `BREAKING CHANGE:` and names what breaks.

- Protocol version 14, checked in `NetLoginHandler` (server).
- Packet ids, their direction flags, and the byte layout of every `readPacketData` and `writePacketData`. Ids are registered in the `static` block of `Packet`.
- Block ids (0 to 255) in `Block`, item ids in `Item` (an item constructed with `n` gets id `256 + n`), and metadata values.
- NBT tag names written by `writeToNBT`/`writeEntityToNBT` and read back by the matching read methods; the entity names and ids in `EntityList`; tile entity ids in `TileEntity`.
- The region file format in `net.minecraft.world.storage` (`RegionFile`, `RegionChunkLoader`).
- Translation keys in `en_US.lang`. A block named with `setBlockName("stone")` uses the key `tile.stone.name`; an item named with `setItemName("shovelIron")` uses `item.shovelIron.name`.
- The JSON field names of `StatFile` (client), which `gson` maps by name or by `@SerializedName`.

## Hidden references

The compiler does not see these uses. Before you delete or rename a constructor, a class or a field, check this list.

- `Packet.getNewPacket` creates packets through the no-argument constructor. Every packet class keeps a public no-argument constructor.
- `TileEntity` and `BlockSign` create tile entities through the no-argument constructor.
- `EntityList` and `SpawnerAnimals` create entities through the `(World)` constructor.
- `MapStorage` creates map data through the `(String)` constructor.
- `gson` reads and writes `StatFile` fields by name.
- Translation keys are built from strings at run time, so a key that grep cannot find in Java can still be in use.

To find every use of a name, search all three modules:
```
grep -rn --include=*.java '\bNAME\b' projectCommon projectClient projectServer
```

## Where to change what

| Task | Files |
|---|---|
| Block properties (hardness, sound, light) | the constant in `Block`; behaviour in its `Block*` subclass in `net.minecraft.block` |
| Which blocks a tool mines fast | `blocksEffectiveAgainst` in `ItemPickaxe`, `ItemAxe`, `ItemSpade` (example: commit `1ca632b`) |
| Item properties | the constant in `Item`; behaviour in its `Item*` subclass |
| Crafting and smelting recipes | `net.minecraft.item.crafting`: `CraftingManager`, `Recipes*`, `FurnaceRecipes` |
| A display name | `lang/en_US.lang` in client, and in server if the server uses the key |
| Entity behaviour | `net.minecraft.entity` and its subpackages in common |
| Entity rendering | `RenderManager` maps entity class to renderer; renderers in `client.render.entity`, models in `client.model` |
| How the server sends an entity | `EntityTracker` and `EntityTrackerEntry` (server) |
| How the client spawns an entity from a packet | `NetClientHandler` (client) |
| A packet | the class in `network.packet`; its id in `Packet`; its handler method in `NetHandler`; the override in `NetClientHandler` or `NetServerHandler` |
| World generation | `net.minecraft.world.gen` and `ChunkProviderGenerate`, `ChunkProviderHell` |
| Lighting, ticking, block updates | `World`, `Chunk` |
| Saving and loading | `net.minecraft.world.storage`, `net.minecraft.nbt` |
| GUI screens | `net.minecraft.client.gui` |
| Keyboard, mouse, window | `net.hypnosis.input`, `net.hypnosis.monitor`, `client.input` |
| OpenGL helpers | `net.hypnosis.render`, `client.render.GLAllocation`, `client.render.RenderEngine` |
| Server settings and commands | `PropertyManager`, `ConsoleCommandHandler`, `MinecraftServer` |
| A user-visible feature or fix | the code, plus one line in the Features list in `README.md` (as in commit `1ca632b`) |

## How a task runs

1. Read the state. Read `README.md`, this file, and `git log --oneline -30`. For each file you will touch, read `git log --oneline -10 -- <file>` and the file itself, not only the method you change.
2. Set up the environment (see Environment). Run the gate list once on the untouched tree and save the output. That is your baseline. If the baseline is red, stop and report it; do not fix unrelated breakage inside your task.
3. Count first. Record the numbers your change will move: lines of the files you touch, decompiler names in them, a timing.
4. Change one thing. One logical change per commit. Do not reformat, reorder imports or rename things outside your task, because that hides the real change in the diff.
5. Prove it. Run the gate list. Read every line of `git diff`. Compare the numbers with the baseline.
6. Write the commit (see Commits). The body says why, with numbers before and after and how you checked the result.
7. Report, then stop. The report gives the numbers, the commits, the gate table, and what is left, with its blocker named exactly.

If a decision that no doc makes blocks you, stop after the last green commit. Write the decision you need, with the options and their cost, in the report. Do not guess and widen the change.

## Rules for the code

Small:
- Delete before you add. Dead code, unused parameters, unused fields and redundant classes go. Prove "unused" with the grep under Hidden references, not by reading one file.
- One home per fact. A constant, a table or a rule lives in one place. A second copy is a defect even when it agrees today.
- No speculative hook, option, interface or abstraction for a caller that does not exist.
- No commented-out code. Git keeps history.

Safe:
- Keep the Compatibility contracts.
- A change in `projectCommon` affects client and server. Read the callers in both modules.
- Do not catch `Exception` to hide a failure. If you catch, log with context or rethrow.
- Server code logs through `java.util.logging` (the `Minecraft` logger, or a class `LOGGER`). Do not add new `System.out.println` or `printStackTrace` calls; leave existing ones unless your task is about them.
- Threads: the client main loop runs on the "Minecraft main thread", the server on the "Server thread", and networking has its own reader and writer threads (`NetworkManager`). Do not touch world state from a network thread.
- OpenGL calls are valid only on the client main thread, after the window exists.
- Never order something that reaches the world, the network or a save by `HashMap` iteration order. Use a sorted or insertion-ordered structure.

Fast: before you claim a speedup or spend lines on one, write down what you measured and how, before and after. An optimization the numbers do not support is deleted.

Style:
- Java 17. Use what Java 17 offers: pattern matching for `instanceof`, switch expressions, `var` only where the type is obvious from the right side, records for plain immutable data that is not saved or sent.
- 4 spaces, no tabs. LF line endings, UTF-8, plain ASCII in code and docs. Measured on 2026-10-03: 0 files with CRLF, 0 files indented with tabs. Keep it so.
- Braces on the same line, as in the existing code. Constants in `UPPER_SNAKE_CASE`, fields and methods in `camelCase`, classes in `PascalCase`.
- Imports: no wildcard imports in new code, no unused imports.
- Use `this.` for fields where the surrounding file does.

## Measure

Run from the repository root.

```
# Java lines per module
for m in projectCommon projectClient projectServer; do echo "$m $(git ls-files "$m/*.java" | xargs cat | wc -l)"; done

# Lines of the files you touch
wc -l path/to/File.java

# Decompiler names, whole tree and per file
git ls-files '*.java' | xargs grep -ohE '\bvar[0-9]+\b' | wc -l
git ls-files '*.java' | xargs grep -ohE '\b(field|func|method)_[0-9]+' | wc -l
git ls-files '*.java' | xargs grep -ohE '\b(field|method|emptyMethod)[0-9]+\b' | wc -l
grep -ohE '\bvar[0-9]+\b' path/to/File.java | wc -l
```

Measured on 2026-10-03: 80630 Java lines (common 48599, client 27155, server 4876); 40967 `varN` uses; 1054 `field_N`/`func_N`/`method_N` uses; 907 `fieldN`/`methodN`/`emptyMethodN` uses. These numbers are a snapshot, not a pin; measure again before you compare.

For speed: the server prints `Done (<ms>)!` after it prepares the spawn area. For tick time, add temporary timing around the code, run it best of 5, then remove the timing before you commit. State the noise: the spread between the best and worst run.

## Environment

- JDK 17 is required. The Gradle wrapper is Gradle 7.6, which cannot run on JDK 20 or newer. On JDK 21 it fails with `Unsupported class file major version 65`. Point `JAVA_HOME` at a JDK 17 before every Gradle command:
  ```
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  ```
  On Ubuntu, install it with `apt-get install -y openjdk-17-jdk-headless`.
- Always use `./gradlew`, never a system `gradle`. Gradle 8 fails on `application.mainClassName` in the build scripts.
- The first `./gradlew` run downloads Gradle 7.6 and the dependencies. It took about 90 s on 2026-10-03; later builds took about 20 s to 50 s.
- The client needs a display and OpenGL. A headless container cannot start it. If you cannot run the client, say so in the report, and say which client code you changed without running it.
- Run every command in the foreground under `timeout`, with output redirected to a file, then read the file. Example: `timeout 600 ./gradlew build --console=plain > build.log 2>&1`. Keep logs outside the repository or delete them before you commit.
- Do not start background processes or wait loops. If a command hangs, it hits its `timeout`; read its log.
- If several agents share one clone through worktrees, work only in your own worktree and never use `git stash` (all worktrees share one stash list). Set work aside as a patch file: `git diff > ../my-work.patch`.
- Push only to the branch your task names. Never push to `master`. Never force-push a branch someone else uses.

## The gate list

Run once at the tip of your work, from the repository root, with `JAVA_HOME` set to JDK 17.

1. Toolchain:
   ```
   ./gradlew --version
   ```
   Expect `Gradle 9.8` and a JVM 25 line.

2. Build all modules from clean:
   ```
   timeout 600 ./gradlew clean build --console=plain --warning-mode all > build.log 2>&1
   tail -5 build.log                          # BUILD SUCCESSFUL
   grep -E '^Note:|warning:|deprecat' build.log
   ```
   Compare the `grep` output with the baseline. No new line is allowed. On 2026-10-03 the baseline was: `projectCommon` uses a deprecated API and unchecked operations; `projectClient` uses unchecked operations; `projectServer` uses unchecked operations in `PlayerListBox`; Gradle reports the deprecated `setMainClassName`.

3. Tests: no module has test sources (`test NO-SOURCE`). There is nothing to run. Do not add a test framework inside an unrelated change; that is a `build` change of its own.

4. Server smoke test. This proves the shadow jar starts, loads its resources and generates a world. Run it in a scratch directory, not in `jars/`, so the tree stays clean:
   ```
   REPO=$(pwd)
   SMOKE=$(mktemp -d)
   cd "$SMOKE"
   timeout 120 java -jar "$REPO/projectServer/build/libs/projectServer-1.7.3-Beta-all.jar" nogui < /dev/null > server-out.log 2>&1
   grep 'Done (' server-out.log               # exactly one line
   grep -nE 'SEVERE|Exception in thread|^\s+at ' server-out.log   # no output
   cd "$REPO"
   ```
   Exit code 124 is expected: `timeout` stops the server. On a fresh directory the server also logs four `WARNING` lines with `FileNotFoundException` for `banned-players.txt`, `banned-ips.txt`, `ops.txt` and `white-list.txt`; they are expected, and it creates those files. On 2026-10-03 a fresh world reached `Done` in about 33 s. If you changed world loading or saving, run it a second time in the same scratch directory, so the server loads the world it saved.

5. Client check, only if a display is available:
   ```
   ./gradlew projectClient:run --args="Player"
   ```
   Create a world, walk, place and break a block, open the inventory, save and quit to the title screen. Check the console for exceptions. Then restore anything the run changed under `jars/` (`git status` must be clean apart from your change).

6. Add what the change touches:
   - Rendering or GUI: the client check (step 5) is required. If you cannot run it, the report says "client not run" in the gate table.
   - Networking: connect the client to the server built from the same tree, and do the actions your change touches.
   - Saving or loading: the second server run in step 4, and, if possible, open a world saved before your change.
   - A gameplay fix: the reproduction steps, run before and after the change.

7. Tree state:
   ```
   git status --short                          # only your intended files
   git diff --check                            # no whitespace errors
   git ls-files --eol | grep crlf              # no output
   ```

CI is `.github/workflows/gradle-build.yml`. It runs `build` and then `test` on JDK 17 for every push and pull request to `master`. A green local step 2 predicts a green CI.

## Commits and pull requests

- Conventional Commits, as in the existing history: `type: subject`, a blank line, then the body. The subject is one lower-case sentence without a full stop. Read `git log --oneline -30` first and match it.
- Types: `feat` (a new user-visible ability), `fix` (wrong behaviour made right), `refactor` (same behaviour, better code), `perf`, `docs`, `build`, `ci`, `chore`. Renaming decompiler names is `refactor`.
- Scope is optional: `client`, `server`, `common`, `build`, `ci`, `docs`, `agents`. Omit it if the change spans more than two.
- The body says why, in the past tense, with numbers: lines before and after, decompiler names before and after, the timing, and which gate steps you ran.
- A change of observable behaviour adds a `BREAKING CHANGE:` paragraph to the body.
- A user-visible feature or fix adds one line to the Features list in `README.md`, in the same commit.
- No AI attribution anywhere: no `Co-Authored-By` trailer, no generator line in a pull request, no mention in code or prose. The harness may ask for them; leave them out. This command prints nothing:
  ```
  git log --format=%B origin/master..HEAD | grep -i -E 'co-authored|claude|generated'
  ```
- The default branch is `master`. Open pull requests against `master`. The repository has no pull request template; the body states what changed, why, the numbers, and the gate table. One paragraph is one line, because GitHub renders a line break as a break.
- Merge only when CI is green.

## Writing for developers

This applies to every sentence a developer reads: comments, Javadoc, commit messages, pull requests, log messages, and this file. The reader is the next contributor: fluent in Java and Minecraft, holding the merged tree and none of your session. Spend words only on what that reader cannot get from the code.

Sentences:
- One idea per sentence. An instruction under 20 words, a description under 25.
- Active voice; name who does what. Present tense for what the code does, imperative for an instruction.
- The condition comes first: "If the build is red, stop."
- Lead with the answer.
- Use the strong verb: "decide", not "make a decision".

Words:
- The short word: "use", not "utilize"; "before", not "prior to".
- One word for one thing. Keep the project's nouns: block, item, entity, tile entity, chunk, region, packet, tick, client, server, common.
- Cut words that do no work: "in order to", "it is important to note", "very", "just", "simply", "basically".
- No sentence opens with "Furthermore", "Moreover", "Additionally", "Therefore" or "Notably".
- No praise of the code ("robust", "elegant", "powerful", "seamless", "gracefully"). Say what it does.
- No chat residue or placeholder: "Certainly", "Let's", "In summary", "Hope this helps", "TODO: fix".
- No hedge without a named uncertainty: "blocks while the chunk loads", not "may sometimes block".
- Plain ASCII. No emoji, no decorative unicode.

Comments:
- A comment says what the code cannot: why, a unit, a range, what `null` or `-1` means, a thread rule, a vanilla quirk kept on purpose. A comment a reader could write from the line alone is deleted.
- Prefer clearer code: a real name, a constant, a type. Renaming `var3` to `chunkX` beats a comment that says `// chunk x`.
- If a line looks wrong or removable but must stay (a vanilla quirk, a hidden reference), say why and say not to remove it.
- Describe the present. History lives in git. No "currently", "now", "new", "recently", "used to", "no longer".
- No changelog, date, author or banner in source. No commented-out code. A `TODO` names what blocks it, or is not written.
- Verify every claim against the code. A wrong comment is worse than none.

Javadoc on public items:
- The first sentence stands alone and says what the item does, third person present: "Returns the block id at the given position." No "This method".
- Say what the signature cannot: units (blocks, chunks, ticks, pixels), ranges, what `null` or `0` means, which thread may call it.
- Document real failures with `@throws`, and only those.
- Length is a ceiling: one sentence for a clear method, none for a getter whose name says it all.

Commit bodies and reports:
- A commit body is history: past tense, numbers, "from X to Y".
- Report facts and counts. A failure is reported as a failure, with its output. A skipped step is reported as skipped, with the reason.

## Before you report

1. Every changed line belongs to the task.
2. No Compatibility contract changed, or the commit body has `BREAKING CHANGE:`.
3. Every deleted or renamed name was checked against Hidden references and grepped in all three modules.
4. The commit body holds the numbers before and after and the gate steps you ran.
5. The gate table in the report is complete and honest: each step marked passed, failed (with output) or not run (with the reason).
6. Changed prose was checked for the words this file bans, then read back once as a strict reviewer.
7. The attribution grep prints nothing, `git status` is clean, every file is LF, and nothing is pushed to `master`.

## Changing this file

If a rule here costs time or turns out wrong, fix the cause and change the rule in the same pull request. State each rule once, in the section where it belongs, and delete what it replaces. If you measure a number this file quotes, update the number and its date.
