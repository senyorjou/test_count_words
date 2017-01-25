from collections import Counter
import os
import re
import sys



def get_files():
    start_path = '.'
    return [f for f in os.listdir(start_path) if
            os.path.isfile(os.path.join(start_path, f))]


def get_file_raw(filename):
    with open(filename, 'r') as f:
        return f.readlines()


def get_tokens(raw):
    return Counter([word for line in raw for word
                    in re.split('\W+', line)
                    if len(word) > 1])


def analyze(results):
    def score_and_times(obj, key):
        return obj[key]['score'], obj[key]['times']

    return sorted(results, key=lambda x: score_and_times(results, x),
                  reverse=True)


def calc_score(obj, base):
    return int(obj['score'] / base * 100)


if __name__ == '__main__':

    words = {}

    for filename in get_files():
        tokens = get_tokens(get_file_raw(filename))
        for token, times in tokens.items():
            if token not in words:
                words[token] = {}
            words[token][filename] = times

    while True:
        querystr = input('>>> ')
        if querystr == ':quit' or querystr == ':q':
            sys.exit(0)

        results = {}
        for word in querystr.split():
            if word in words:
                for filename, times in words[word].items():
                    if filename not in results:
                        results[filename] = {'score': 0, 'times': 0}
                    results[filename]['score'] += 1
                    results[filename]['times'] += times

        if not results:
            print('No matches found')
        else:
            rated_results = analyze(results)
            results_length = len(results)
            for filename in analyze(results):
                print('{}: {}'.format(filename, calc_score(results[filename],
                                                           results_length)))
