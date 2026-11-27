import java.util.Scanner;

/* 화면과 입력 담당. 위치, 체력, 열쇠 등 게임 데이터는 변경하지 않는다. */
public class GameUI 
{
    private final GameManager manager;
    private final Scanner scanner;

    public GameUI(GameManager manager) 
    {
        this(manager, new Scanner(System.in));
    }

    public GameUI(GameManager manager, Scanner scanner) 
    {
        this.manager = manager;
        this.scanner = scanner;
    }

    public static void main(String[] args) 
    {
        new GameUI(new GameManager()).run();
    }

    public void showMenu() 
    {
        System.out.println("\n========== 미로 탈출 게임 ==========");
        System.out.println("1. 게임 시작");
        System.out.println("2. 도움말");
        System.out.println("3. 종료");
    }

    public void showHelp() 
    {
        System.out.println("\n[게임 방법]");
        System.out.println("W: 위 / A: 왼쪽 / S: 아래 / D: 오른쪽");
        System.out.println("R: 현재 스테이지 재시작 / Q: 종료");
        System.out.println("한 글자 입력 후 Enter를 누르세요. 대소문자는 구분하지 않습니다.");
        System.out.println("#: 벽  .: 통로  P: 플레이어  E: 출구");
        System.out.println("K: 열쇠  D: 잠긴 문  O: 열린 문  T: 함정");
        System.out.println("문을 열 때 열쇠 1개를 사용합니다. 열린 문은 계속 통과할 수 있습니다.");
        System.out.println("함정에 들어갈 때마다 체력이 20 감소하고, 0이면 게임 오버입니다.");
        System.out.println("이동에 성공했을 때만 이동 횟수가 증가합니다.");
        System.out.println("새 스테이지와 재시작 시 체력 100, 열쇠 0, 이동 횟수 0으로 초기화됩니다.");
        System.out.println("총 3개 스테이지의 출구에 도착하면 최종 승리합니다.");
    }

    /* 게임 진행 중 올바른 한 글자만 반환한다. EOF는 종료로 처리한다. */
    public char readCommand() 
    {
        return readChoice("WASDRQ", "이동 W/A/S/D | 재시작 R | 종료 Q > ", 'Q', true);
    }

    private char readChoice(String allowed, String prompt, char eof, boolean inGame) 
    {
        while (true) 
        {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) 
            	return eof;
            String line = scanner.nextLine().trim();
            if (line.length() == 1) 
            {
                char command = Character.toUpperCase(line.charAt(0));
                if (allowed.indexOf(command) >= 0) 
                	return command;
            }
            System.out.println("잘못된 입력입니다. 가능한 명령 중 한 글자만 입력하세요.");
            if (inGame) render(manager.getMaze(), manager.getPlayer());
        }
    }

    /* 모델은 읽기만 한다. 플레이어를 해당 칸의 요소 위에 표시한다. */
    public void render(Maze maze, Player player) {
        System.out.printf("\n[스테이지 %d / 3] 체력: %d / 100 | 열쇠: %d | 이동 횟수: %d%n",
                manager.getStageNumber(), player.getHp(), player.getKeys(), player.getMoves());
        for (int row = 0; row < maze.getRows(); row++) 
        {
            for (int col = 0; col < maze.getCols(); col++) 
            {
                if (row == player.getRow() && col == player.getCol()) 
                {
                    player.draw();
                } 
                else 
                {
                    GraphicsObject cell = maze.getCell(row, col);
                    if (cell == null) System.out.print('.');
                    else cell.draw();
                }
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("#:벽 .:통로 P:나 E:출구 K:열쇠 D:잠긴 문 O:열린 문 T:함정");
    }

    public void showResult(int result) 
    {
        switch (result) 
        {
            case GameManager.MOVED: System.out.println("한 칸 이동했습니다."); break;
            case GameManager.BLOCKED_WALL: System.out.println("벽으로는 이동할 수 없습니다."); break;
            case GameManager.BLOCKED_OUT_OF_RANGE: System.out.println("미로 범위 밖으로는 이동할 수 없습니다."); break;
            case GameManager.BLOCKED_LOCKED_DOOR: System.out.println("잠긴 문입니다. 열쇠가 필요합니다."); break;
            case GameManager.KEY_ACQUIRED: System.out.println("열쇠를 획득했습니다!"); break;
            case GameManager.DOOR_OPENED: System.out.println("열쇠 1개로 문을 열었습니다!"); break;
            case GameManager.TRAP_HIT: System.out.println("함정을 밟았습니다. 체력이 20 감소했습니다."); break;
            case GameManager.STAGE_CLEAR:
                System.out.printf("스테이지 %d 완료! 이동 횟수: %d%n",
                        manager.getStageNumber(), manager.getPlayer().getMoves());
                break;
            case GameManager.GAME_OVER: System.out.println("체력이 0이 되었습니다. 게임 오버!"); break;
            case GameManager.VICTORY:
                System.out.printf("최종 승리! 3개 스테이지를 모두 탈출했습니다! 마지막 스테이지 이동 횟수: %d%n",
                        manager.getPlayer().getMoves());
                break;
            default: throw new IllegalArgumentException("정의되지 않은 이동 결과: " + result);
        }
    }

    public void run() 
    {
        while (true) 
        {
            showMenu();
            char menu = readChoice("123", "메뉴 선택 > ", '3', false);
            if (menu == '3') break;
            if (menu == '2') 
            {
                showHelp();
                continue;
            }
            manager.startStage(0);
            if (!play()) break;
        }
        System.out.println("게임을 종료합니다.");
    }

    /* 최종 승리 후 true(메뉴 복귀), Q 또는 EOF이면 false(프로그램 종료). */
    private boolean play() 
    {
        render(manager.getMaze(), manager.getPlayer());
        while (true) 
        {
            if (manager.getStatus() == GameManager.STATUS_VICTORY) return true;
            if (manager.getStatus() == GameManager.STATUS_STAGE_CLEAR) 
            {
                // 완료 횟수와 최종 화면을 먼저 출력한 다음 상태를 초기화한다.
                if (!manager.nextStage()) return true;
                System.out.println("다음 스테이지를 시작합니다.");
                render(manager.getMaze(), manager.getPlayer());
                continue;
            }
            char command;
            if (manager.getStatus() == GameManager.STATUS_GAME_OVER) 
            {
                command = readChoice("RQ", "게임 오버 | 재시작 R | 종료 Q > ", 'Q', true);
            } 
            else 
            {
                command = readCommand();
            }
            if (command == 'Q') return false;
            if (command == 'R') 
            {
                manager.restartStage();
                System.out.println("현재 스테이지를 다시 시작합니다.");
            } 
            else 
            {
                showResult(manager.move(command));
            }
            render(manager.getMaze(), manager.getPlayer());
        }
    }
}
