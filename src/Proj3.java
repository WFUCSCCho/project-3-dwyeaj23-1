import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Proj3 {

    // Merge Sort
    public static <T extends Comparable<T>> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
    }

    public static <T extends Comparable<T>> void merge(ArrayList<T> a, int left, int mid, int right) {
        ArrayList<T> temp = new ArrayList<>(a);
        int i = left, j = mid + 1, k = left;

        while (i <= mid && j <= right) {
            if (temp.get(i).compareTo(temp.get(j)) <= 0) {
                a.set(k++, temp.get(i++));
            } else {
                a.set(k++, temp.get(j++));
            }
        }

        while (i <= mid) a.set(k++, temp.get(i++));
        while (j <= right) a.set(k++, temp.get(j++));
    }

    // Quick Sort
    public static <T extends Comparable<T>> void quickSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(a, left, right);
            quickSort(a, left, pivotIndex - 1);
            quickSort(a, pivotIndex + 1, right);
        }
    }

    public static <T extends Comparable<T>> int partition(ArrayList<T> a, int left, int right) {
        T pivot = a.get(right);
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (a.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, right);
        return i + 1;
    }

    static <T> void swap(ArrayList<T> a, int i, int j) {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // Heap Sort
    public static <T extends Comparable<T>> void heapSort(ArrayList<T> a, int size) {
        for (int i = size / 2 - 1; i >= 0; i--) heapify(a, size, i);
        for (int i = size - 1; i > 0; i--) {
            swap(a, 0, i);
            heapify(a, i, 0);
        }
    }

    public static <T extends Comparable<T>> void heapify(ArrayList<T> a, int size, int root) {
        int largest = root, left = 2 * root + 1, right = 2 * root + 2;

        if (left < size && a.get(left).compareTo(a.get(largest)) > 0) largest = left;
        if (right < size && a.get(right).compareTo(a.get(largest)) > 0) largest = right;

        if (largest != root) {
            swap(a, root, largest);
            heapify(a, size, largest);
        }
    }

    // Bubble Sort
    public static <T extends Comparable<T>> int bubbleSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < size; i++) {
                comparisons++;
                if (a.get(i - 1).compareTo(a.get(i)) > 0) {
                    swap(a, i - 1, i);
                    swapped = true;
                }
            }
        } while (swapped);
        return comparisons;
    }

    // Odd-Even Transposition Sort
    public static <T extends Comparable<T>> int transpositionSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        boolean isSorted = false;

        while (!isSorted) {
            isSorted = true;

            // Odd indexed pass
            for (int i = 1; i < size - 1; i += 2) {
                comparisons++;
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    isSorted = false;
                }
            }

            // Even indexed pass
            for (int i = 0; i < size - 1; i += 2) {
                comparisons++;
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    isSorted = false;
                }
            }
        }
        return comparisons;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: java Proj3 <input file> <sorting algorithm> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        String algorithm = args[1].toLowerCase();
        int numLines = Integer.parseInt(args[2]);

        ArrayList<Pokemon> pokemonList = new ArrayList<>();
        loadPokemonData(inputFileName, pokemonList, numLines);

        ArrayList<Pokemon> sortedPokemon = new ArrayList<>(pokemonList);
        ArrayList<Pokemon> shuffledPokemon = new ArrayList<>(pokemonList);
        ArrayList<Pokemon> reversedPokemon = new ArrayList<>(pokemonList);

        Collections.sort(sortedPokemon);
        Collections.shuffle(shuffledPokemon);
        Collections.sort(reversedPokemon, Collections.reverseOrder());

        long startTime, endTime;
        int comparisons = 0;

        switch (algorithm) {
            case "bubble":
                comparisons = bubbleSort(shuffledPokemon, shuffledPokemon.size());
                break;
            case "merge":
                startTime = System.nanoTime();
                mergeSort(shuffledPokemon, 0, shuffledPokemon.size() - 1);
                endTime = System.nanoTime();
                System.out.println("Merge Sort took: " + (endTime - startTime) / 1e9 + " seconds");
                break;
            case "quick":
                startTime = System.nanoTime();
                quickSort(shuffledPokemon, 0, shuffledPokemon.size() - 1);
                endTime = System.nanoTime();
                System.out.println("Quick Sort took: " + (endTime - startTime) / 1e9 + " seconds");
                break;
            case "heap":
                startTime = System.nanoTime();
                heapSort(shuffledPokemon, shuffledPokemon.size());
                endTime = System.nanoTime();
                System.out.println("Heap Sort took: " + (endTime - startTime) / 1e9 + " seconds");
                break;
            case "transposition":
                comparisons = transpositionSort(shuffledPokemon, shuffledPokemon.size());
                break;
            default:
                System.err.println("Unknown sorting algorithm: " + algorithm);
                System.exit(1);
        }

        // Log results to analysis.txt
        try (FileOutputStream output = new FileOutputStream("analysis.txt", true)) {
            String result = String.format("%s,%d,%d\n", algorithm, numLines, comparisons);
            output.write(result.getBytes());
        }
    }

    // Method to load Pokemon data from the CSV file
    private static void loadPokemonData(String csvFile, ArrayList<Pokemon> pokemonList, int numLines) {
        try (Scanner scanner = new Scanner(new File(csvFile))) {
            // Skip the first line (header)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            int count = 0;
            while (scanner.hasNextLine() && count < numLines) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Assuming CSV format: id,name,type1,type2,total,hp,attack,defense,specialAttack,specialDefense,speed,generation,isLegendary
                String[] attributes = line.split(",");
                if (attributes.length >= 13) {  // Ensure there are enough columns
                    int id = Integer.parseInt(attributes[0]);
                    String name = attributes[1];
                    String type1 = attributes[2];
                    String type2 = attributes[3].equals("None") ? "" : attributes[3];
                    int total = Integer.parseInt(attributes[4]);
                    int hp = Integer.parseInt(attributes[5]);
                    int attack = Integer.parseInt(attributes[6]);
                    int defense = Integer.parseInt(attributes[7]);
                    int specialAttack = Integer.parseInt(attributes[8]);
                    int specialDefense = Integer.parseInt(attributes[9]);
                    int speed = Integer.parseInt(attributes[10]);
                    int generation = Integer.parseInt(attributes[11]);
                    boolean isLegendary = Boolean.parseBoolean(attributes[12]);

                    // Create a new Pokemon object and add it to the list
                    Pokemon pokemon = new Pokemon(id, name, type1, type2, total, hp, attack, defense,
                            specialAttack, specialDefense, speed, generation, isLegendary);
                    pokemonList.add(pokemon);
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + csvFile);
        }
    }
}