from collections import Counter
import os
import re
import sys


class File(object):
    def __init__(self, filename):
        self.filename = filename
        self.raw = self.read_contents()
        self.words = self.get_words()

    def __repr__(self):
        return '{} ({} lines, {} words)'.format(self.filename, len(self.raw),
                                                len(self.words))

    def get_words(self):
        return Counter([word for line in self.raw for word
                        in re.split('\W+', line)
                        if len(word) > 1])

    def read_contents(self):
        with open(self.filename, 'r') as f:
            return f.readlines()

    def score(self, word):
        return self.words.get(word, 0)


def get_files():
    start_path = '.'
    return [File(f) for f in os.listdir(start_path) if
            os.path.isfile(os.path.join(start_path, f))]


def analyze(inputstr, files):
    return [file.score(inputstr) for file in files]

if __name__ == '__main__':
    files = [file for file in get_files()]

    while True:
        inputstr = input('>>> ')
        if inputstr == ':quit' or inputstr == ':q':
            sys.exit(0)
        for file in files:
            print('{}: {}'.format(file.filename, file.score(inputstr)))
