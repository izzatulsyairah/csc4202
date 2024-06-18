import java.util.ArrayList;
import java.util.List;

class Patient {
    int id;
    int severity;
    int neededBeds;
    int neededVents;
    float survivalWithICU;
    float survivalWithoutICU;

    public Patient(int id, int severity, int neededBeds, int neededVents, float survivalWithICU, float survivalWithoutICU) {
        this.id = id;
        this.severity = severity;
        this.neededBeds = neededBeds;
        this.neededVents = neededVents;
        this.survivalWithICU = survivalWithICU;
        this.survivalWithoutICU = survivalWithoutICU;
    }

    public int getNeededBeds() {
        return neededBeds;
    }

    public int getNeededVents() {
        return neededVents;
    }

    public float getSurvivalWithICU() {
        return survivalWithICU;
    }

    public float getSurvivalWithoutICU() {
        return survivalWithoutICU;
    }
}

public class ResourceAllocator {
    public static float allocateResources(int maxTime, int beds, int vents, List<Patient> patients) {
        float[][][] dp = new float[maxTime + 1][beds + 1][vents + 1];

        for (int t = 1; t <= maxTime; t++) {
            for (int b = 0; b <= beds; b++) {
                for (int v = 0; v <= vents; v++) {
                    for (Patient p : patients) {
                        int neededBeds = p.getNeededBeds();
                        int neededVents = p.getNeededVents();
                        if (b >= neededBeds && v >= neededVents) {
                            float immediateBenefit = benefitFunction(p);
                            int remainingBeds = b - neededBeds;
                            int remainingVents = v - neededVents;
                            dp[t][b][v] = Math.max(dp[t][b][v], immediateBenefit + dp[t - 1][remainingBeds][remainingVents]);
                        }
                    }
                }
            }
        }

        float maximumBenefit = 0;
        for (int b = 0; b <= beds; b++) {
            for (int v = 0; v <= vents; v++) {
                maximumBenefit = Math.max(maximumBenefit, dp[maxTime][b][v]);
            }
        }

        return maximumBenefit;
    }

    public static float benefitFunction(Patient patient) {
        return patient.getSurvivalWithICU() - patient.getSurvivalWithoutICU();
    }

    public static void main(String[] args) {
        int maxTime = 3;
        int beds = 2;
        int vents = 1;

        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient(1, 1, 1, 0, 0.9f, 0.5f));
        patients.add(new Patient(2, 2, 1, 1, 0.8f, 0.3f));
        patients.add(new Patient(3, 3, 2, 1, 0.7f, 0.2f));

        float result = allocateResources(maxTime, beds, vents, patients);
        System.out.println("Maximum Benefit: " + result);
    }
}
