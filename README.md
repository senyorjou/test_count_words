##### Code for Scala coding test 

The solution relies on an inverted index solution. Creating a `Map` with the words and a corresponding `List` of files that contains the word.

When searching for words the script simply filters map keys against input tokens and stores filenames on a List.

Later filenames ara aggregated and sorted, giving the most simple rank solution.

|   | file A  | file B  | file C  |
|---|:-:|:-:|:-:|
| foo  | X  |   | X  |
| bar  | X | X |   |
| foobar  |   | X |   |

```
$ CountWApp foo bar
file A : 2
file B : 1
```



Code DOES NOT perform all requirements, here's list what's missing

- Score calculation is simple, no % based on complex matching
- Word definition is a simple `\\W` regexp match
- Word fetching lowercases by default and ignores < 2 chars words. No blacklist.
- Files with no matching words do not appear on listing
- No testing
- Lack of error checking

