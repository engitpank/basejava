import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage, null);
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                storage[i] = r;
                break;
            }
        }
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume != null && resume.uuid.equals(uuid))
                return resume;
        }
        return null;
    }

    void delete(String uuid) {
        int idDeletedResume = -1;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null && storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                idDeletedResume = i;
                break;
            }
        }
        if (idDeletedResume != -1) {
            for (int i = idDeletedResume; i < storage.length - 1; i++) {
                if (storage[i + 1] == null) {
                    break;
                } else {
                    storage[i] = storage[i + 1];
                    storage[i + 1] = null;
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] allResume;

        allResume = new Resume[size()];

        for (int i = 0; i < allResume.length; i++) {
            allResume[i] = storage[i];
        }
        return allResume;
    }

    int size() {
        int size = 0;
        for (Resume resume : storage) {
            if (resume != null) {
                size += 1;
            }
        }
        return size;
    }
}
