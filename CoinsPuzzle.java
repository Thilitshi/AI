// Student surname :Mudzungwane
//Student name :Thilitshi
// Student number :4335400
//CSC311 2025 AI Practical  
import java.io.*;
import java.util.*;

class State implements Comparable<State> {
    private char[] board;
    private int hValue;

    public State(char[] board) {
        this.board = Arrays.copyOf(board, board.length);
        this.hValue = calculateHeuristic();
    }

    public static State readInitialState(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine().trim();
            char[] board = line.toCharArray();
            return new State(board);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int calculateHeuristic() {
        // Goal state: "BBB0RRR"
        char[] goal = {'B', 'B', 'B', '0', 'R', 'R', 'R'};
        int misplaced = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != goal[i]) misplaced++;
        }
        return misplaced;
    }

    public int getHValue() {
        return hValue;
    }

    public List<State> generateChildren() {
        List<State> children = new ArrayList<>();
        int zeroPos = findZero();
        int[] moves = {-1, 1, -2, 2};

        for (int move : moves) {
            int newPos = zeroPos + move;
            if (isValidMove(newPos)) {
                char[] newBoard = Arrays.copyOf(board, board.length);
                newBoard[zeroPos] = newBoard[newPos];
                newBoard[newPos] = '0';
                children.add(new State(newBoard));
            }
        }
        return children;
    }

    private int findZero() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '0') return i;
        }
        return -1;
    }

    private boolean isValidMove(int pos) {
        return pos >= 0 && pos < board.length;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.hValue, other.hValue);
    }

    @Override
    public String toString() {
        return new String(board);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        State state = (State) obj;
        return Arrays.equals(board, state.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}

public class CoinsPuzzle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the input file name: ");
        String inputFile = scanner.nextLine();
        System.out.print("Enter the output file name: ");
        String outputFile = scanner.nextLine();

        State startState = State.readInitialState(inputFile);
        if (startState == null) {
            System.out.println("Error reading input file.");
            return;
        }

        PriorityQueue<State> queue = new PriorityQueue<>();
        queue.add(startState);

        Map<String, Integer> visited = new HashMap<>();
        visited.put(startState.toString(), 0);

        int moves = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            while (!queue.isEmpty()) {
                State currentState = queue.poll();
                moves = visited.get(currentState.toString());

                writer.write("h=" + currentState.getHValue() + " " + currentState);
                writer.newLine();
                System.out.println("h=" + currentState.getHValue() + " " + currentState);

                if (currentState.getHValue() == 0) {
                    String goalMessage = "Goal state reached in " + moves + " moves.";
                    System.out.println(goalMessage);
                    writer.write(goalMessage);
                    writer.newLine();
                    writer.write("Final state: " + currentState);
                    writer.newLine();
                    break;
                }

                for (State child : currentState.generateChildren()) {
                    if (!visited.containsKey(child.toString())) {
                        visited.put(child.toString(), moves + 1);
                        queue.add(child);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


